package com.example.doantotnghiep.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetPassword {


    private String token;
    @NotBlank
    private String email;

    @NotBlank
    private String newpassword;
}
