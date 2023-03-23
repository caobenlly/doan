package com.example.doantotnghiep.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseRespone {
    private String message;

    public BaseRespone(String message) {
        this.message = message;
    }
}
