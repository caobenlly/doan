package com.example.doantotnghiep.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter
@AllArgsConstructor
public class ChartDTO {
    private Date label;
    private Long value;


}
