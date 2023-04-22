package com.example.doantotnghiep.service.impl;


import com.example.doantotnghiep.entity.RefreshToken;
import com.example.doantotnghiep.entity.User;
import com.example.doantotnghiep.entity.exception.BadRequestException;
import com.example.doantotnghiep.responsitory.RefreshTokenRepository;
import com.example.doantotnghiep.responsitory.UserRepository;
import com.example.doantotnghiep.security.CustomUserDetails;
import com.example.doantotnghiep.security.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class RefreshTokenService {

    private static final String PREFIX_TOKEN = "Bearer";
    private static final String AUTHORIZATION = "Authorization";
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {

        Optional<User> user = userRepository.findById(userId);

        if (user == null){
            throw new BadRequestException("Không có người dùng");
        }

        try {

            String token = jwtTokenUtil.generateToken((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal());

            String jwt = PREFIX_TOKEN + " " + token;
            Claims claims = jwtTokenUtil.getClaimsFromToken(token);
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken(jwt);
            refreshToken.setExpiryDate(claims.getExpiration());
            refreshTokenRepository.save(refreshToken);
            return refreshToken;

        } catch (Exception ex) {
            throw new BadRequestException("Không có người dùng");

        }

    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Date.from(Instant.now())) < 0) {
            refreshTokenRepository.delete(token);

            throw new BadRequestException("Refresh token was expired. Please make a new signin request");

        }

        return token;
    }

    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }
}
