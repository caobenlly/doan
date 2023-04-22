package com.example.doantotnghiep.service.impl;


import com.example.doantotnghiep.entity.MainResponse;
import com.example.doantotnghiep.entity.ResetPasswordToken;
import com.example.doantotnghiep.entity.User;
import com.example.doantotnghiep.event.OnResetPasswordViaEmailEvent;
import com.example.doantotnghiep.entity.exception.AppException;
import com.example.doantotnghiep.entity.exception.BadRequestException;
import com.example.doantotnghiep.entity.exception.ErrorResponseBase;
import com.example.doantotnghiep.model.dto.UserDTO;
import com.example.doantotnghiep.model.mapper.UserMapper;
import com.example.doantotnghiep.model.request.*;
import com.example.doantotnghiep.responsitory.ResetPasswordTokenRepository;
import com.example.doantotnghiep.responsitory.UserRepository;
import com.example.doantotnghiep.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Log4j2
public class UserServiceImpl implements UserService {


    @Autowired
    UserRepository userRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ResetPasswordTokenRepository resetPasswordTokenRepository;
    @Override
    public List<UserDTO> getListUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOS.add(UserMapper.toUserDTO(user));
        }
        return userDTOS;
    }

    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        User user = userRepository.findByEmail(createUserRequest.getEmail());
        if (user != null) {
            throw new BadRequestException("Email đã tồn tại trong hệ thống. Vui lòng sử dụng email khác!");
        }
        user = UserMapper.toUser(createUserRequest);
        userRepository.save(user);
        return user;
    }

    @Override
    public List<User> adminListUserPages(String fullName, String phone, String email) {
        return userRepository.adminListUserPages(fullName, phone, email);
    }



    @Override
    public void changePassword(User user, ChangePasswordRequest changePasswordRequest) {
        //Kiểm tra mật khẩu
        if (!BCrypt.checkpw(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Mật khẩu cũ không chính xác");
        }

        String hash = BCrypt.hashpw(changePasswordRequest.getNewPassword(), BCrypt.gensalt(12));
        user.setPassword(hash);
        userRepository.save(user);
    }

    @Override
    public User updateProfile(User user, UpdateProfileRequest updateProfileRequest) {
        user.setFullName(updateProfileRequest.getFullName());
        user.setPhone(updateProfileRequest.getPhone());
        user.setAddress(updateProfileRequest.getAddress());

        return userRepository.save(user);
    }
    @Override
    public MainResponse resetPasswordViaEmail(String email) {
        log.info("Quên mật khẩu");
        // find user by email
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new AppException(ErrorResponseBase.USER_NOT_EXISTED);
        }


        String resetPasswordToken = resetPasswordTokenRepository.findByUserId(user.getId());
        if( resetPasswordToken!= null){
            // remove Reset Password
            ResetPasswordToken resetPasswordToken1 = resetPasswordTokenRepository.findByToken(resetPasswordToken);
            int otp = (int) Math.floor(((Math.random() * 899999) + 100000));
            resetPasswordToken1.setOtp(otp);
        }else {
            // create new reset password OTP
            createNewResetPasswordOTP(user);
        }
        // send email
        sendResetPasswordViaEmail(email);


        //get token

        String otp = resetPasswordTokenRepository.findByUserId(user.getId());
        MainResponse response = new MainResponse();
        response.setToken(otp);
        response.setMessage("OTP đã được gửi vui lòng check email");

        return response;
    }

    @Override
    public void sendResetPasswordViaEmail(String email) {
        eventPublisher.publishEvent(new OnResetPasswordViaEmailEvent(email));
    }

    private void createNewResetPasswordOTP(User user) {


        // create new token for Reseting password
        final int newToken = (int) Math.floor(((Math.random() * 899999) + 100000));
        final String newTokenUser = UUID.randomUUID().toString();
        ResetPasswordToken token = new ResetPasswordToken(newToken, user,newTokenUser);

        resetPasswordTokenRepository.save(token);

    }

    @Override
    public User findUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }


    @Override
    public MainResponse authentificationotp(AuthentificationOtp authentificationOtp) {
        log.info("xác thực otp");


        int otpauthen = resetPasswordTokenRepository.findByOtp( findUserByEmail(authentificationOtp.getEmail()).getId());


        String s = String.valueOf(otpauthen);
        if (authentificationOtp.getOtp().equals(s) == false ){
            throw new AppException(ErrorResponseBase.NOT_OTP);
        }

        if(findUserByEmail(authentificationOtp.getEmail())==null){
            throw  new AppException(ErrorResponseBase.NOT_EXISTED);
        }
        User user = findUserByEmail(authentificationOtp.getEmail());

        String token = resetPasswordTokenRepository.findByUserId(user.getId());
        MainResponse response = new MainResponse();
        response.setToken(token);
        response.setMessage("Xác thực thành công");
        return response;
    }

    @Override
    public MainResponse resetPassword(ResetPassword resetPassword) {

        log.info("đổi mật khẩu");
// get token
        ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findByToken(resetPassword.getToken());

        if(findUserByEmail(resetPassword.getEmail()) == null){
            throw new AppException(ErrorResponseBase.NOT_FOUND);
        }
        // change password
        User user = resetPasswordToken.getUser();
        user.setPassword(passwordEncoder.encode(resetPassword.getNewpassword()));
        userRepository.save(user);

        // remove Reset Password
        resetPasswordTokenRepository.deleteById(resetPasswordToken.getId());


        MainResponse response = new MainResponse();
        response.setToken(resetPassword.getToken());
        response.setMessage("Đổi mật khẩu thành công");
        return response;
    }
}
