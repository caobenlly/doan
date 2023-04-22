package com.example.doantotnghiep.controller.shop;


import com.example.doantotnghiep.entity.RefreshToken;
import com.example.doantotnghiep.entity.User;
import com.example.doantotnghiep.entity.exception.BadRequestException;
import com.example.doantotnghiep.model.dto.BaseRespone;
import com.example.doantotnghiep.model.dto.UserDTO;
import com.example.doantotnghiep.model.mapper.UserMapper;
import com.example.doantotnghiep.model.request.*;
import com.example.doantotnghiep.responsitory.RefreshTokenRepository;
import com.example.doantotnghiep.security.CustomUserDetails;
import com.example.doantotnghiep.security.JwtTokenUtil;
import com.example.doantotnghiep.service.UserService;
import com.example.doantotnghiep.service.impl.RefreshTokenService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.doantotnghiep.config.Contant.MAX_AGE_COOKIE;
@CrossOrigin("*")
@RestController
@RequestMapping("")
@Validated
public class  UserController {

    private static final String PREFIX_TOKEN = "Bearer";
    private static final String AUTHORIZATION = "Authorization";
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @GetMapping("/users")
    public ResponseEntity<Object> getListUsers() {
        List<UserDTO> userDTOS = userService.getListUsers();
        return ResponseEntity.ok(userDTOS);
    }

    @PostMapping("/api/admin/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody CreateUserRequest createUserRequest){
        User user = userService.createUser(createUserRequest);
        return ResponseEntity.ok(UserMapper.toUserDTO(user));
    }

    @PostMapping("/api/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        //Authenticate
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            ));
            //Gen token
            String token = jwtTokenUtil.generateToken((CustomUserDetails) authentication.getPrincipal());
               List<String> respon = new ArrayList<>();
//            //Add token to cookie to login
//            Cookie cookie = new Cookie("JWT_TOKEN", token);
//            cookie.setMaxAge(MAX_AGE_COOKIE);
//            cookie.setPath("/");
//            response.addCookie(cookie);

            //add token to header to login
//            response.addHeader(AUTHORIZATION, PREFIX_TOKEN + " " + token);
            String jwt = PREFIX_TOKEN + " " + token;
            UserDTO userDTO = UserMapper.toUserDTO(((CustomUserDetails) authentication.getPrincipal()).getUser());
            userDTO.setToken(jwt);
            Claims claims = jwtTokenUtil.getClaimsFromToken(token);
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken(jwt);
            refreshToken.setExpiryDate(claims.getExpiration());
            refreshTokenRepository.save(refreshToken);
            userDTO.setExpiry_date(claims.getExpiration());
          return ResponseEntity.ok(userDTO);

        } catch (Exception ex) {
            throw new BadRequestException("Email hoặc mật khẩu không chính xác!");

        }
    }

    @PostMapping("/api/register")
    public ResponseEntity<Object> register(@Valid @RequestBody CreateUserRequest createUserRequest, HttpServletResponse response) {
        //Create user
        User user = userService.createUser(createUserRequest);

        //Gen token
        UserDetails principal = new CustomUserDetails(user);
        String token = jwtTokenUtil.generateToken(principal);

        //Add token on cookie to login
        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setMaxAge(MAX_AGE_COOKIE);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(UserMapper.toUserDTO(user));
    }

    @GetMapping("/tai-khoan")
    public String getProfilePage(Model model) {
        return "shop/account";
    }

    @PostMapping("/api/change-password")
    public ResponseEntity<Object> changePassword(@Valid @RequestBody ChangePasswordRequest passwordReq) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        userService.changePassword(user, passwordReq);
        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }

    @PutMapping("/api/update-profile")
    public ResponseEntity<Object> updateProfile(@Valid @RequestBody UpdateProfileRequest profileReq) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        user = userService.updateProfile(user, profileReq);
        UserDetails userDetails = new CustomUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return ResponseEntity.ok("Cập nhật thành công");
    }

    // reset password confirm
    @PostMapping("/resetPasswordRequest")
    // validate: email exists, email not active
    public ResponseEntity<?> sendResetPasswordViaEmail(@RequestParam("email") String email) {

//        userService.resetPasswordViaEmail(email);


        return new ResponseEntity<>(userService.resetPasswordViaEmail(email), HttpStatus.OK);
    }

    @PostMapping("/authentificationotp")
    // validate: email exists, email not active
    public ResponseEntity<?> authentificationotp(@Valid @RequestBody AuthentificationOtp authentificationOtp) {

        userService.authentificationotp(authentificationOtp);

        return new ResponseEntity<>(userService.authentificationotp(authentificationOtp), HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    // validate: check exists, check not expired
    public ResponseEntity<?> resetPasswordViaEmail(@Valid @RequestBody ResetPassword resetPassword) {

        // reset password
        userService.resetPassword(resetPassword);

         BaseRespone baseRespone = new BaseRespone("Thành Công");
        return new ResponseEntity<>(baseRespone, HttpStatus.OK);
    }


    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getToken();

        Optional<RefreshToken> optionaltoken= refreshTokenService.findByToken(requestRefreshToken);

         //kiểm tra còn h không
        if (optionaltoken.isPresent()){
            refreshTokenService.verifyExpiration(optionaltoken.get());
        }

        RefreshToken refreshTokenRespones = refreshTokenService.createRefreshToken(request.getId());

        return ResponseEntity.ok(refreshTokenRespones);
    }
}
