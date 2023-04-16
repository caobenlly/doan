package com.example.doantotnghiep.model.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDTO {
    private long id;
    private String fullName;
    private String email;
    private String address;
    private String phone;
    private String avatar;
    private String token;
    private List<String> roles;
    private Date expiry_date;
}
