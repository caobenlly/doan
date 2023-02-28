package com.example.doantotnghiep.service;



import com.example.doantotnghiep.entity.MainResponse;
import com.example.doantotnghiep.entity.User;
import com.example.doantotnghiep.model.dto.UserDTO;
import com.example.doantotnghiep.model.request.ChangePasswordRequest;
import com.example.doantotnghiep.model.request.CreateUserRequest;
import com.example.doantotnghiep.model.request.UpdateProfileRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<UserDTO> getListUsers();

    Page<User> adminListUserPages(String fullName, String phone, String email, Integer page);

    User createUser(CreateUserRequest createUserRequest);

    void changePassword(User user, ChangePasswordRequest changePasswordRequest);

    User updateProfile(User user, UpdateProfileRequest updateProfileRequest);
    MainResponse resetPasswordViaEmail(String email);
    void sendResetPasswordViaEmail(String email);
    User findUserByEmail(String email);
}
