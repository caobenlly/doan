package com.example.doantotnghiep.controller.admin;

import com.example.doantotnghiep.service.UserService;
import com.example.doantotnghiep.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin("*")
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin/users")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Object> homePages(Model model,
                            @RequestParam(defaultValue = "", required = false) String fullName,
                            @RequestParam(defaultValue = "", required = false) String phone,
                            @RequestParam(defaultValue = "", required = false) String email,
                            @RequestParam(defaultValue = "", required = false) String address,
                            @RequestParam(defaultValue = "1", required = false) Integer page) {
        List<User> users = (List<User>) userService.adminListUserPages(fullName, phone, email);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/api/admin/users/list")
    public ResponseEntity<Object> getListUserPages(@RequestParam(defaultValue = "", required = false) String fullName,
                                                   @RequestParam(defaultValue = "", required = false) String phone,
                                                   @RequestParam(defaultValue = "", required = false) String email,
                                                   @RequestParam(defaultValue = "", required = false) String address,
                                                   @RequestParam(defaultValue = "1", required = false) Integer page) {
        List<User> users = userService.adminListUserPages(fullName, phone, email);
        return ResponseEntity.ok(users);
    }
}
