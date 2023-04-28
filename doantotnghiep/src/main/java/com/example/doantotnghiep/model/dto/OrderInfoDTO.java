package com.example.doantotnghiep.model.dto;

import com.example.doantotnghiep.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoDTO {
    private long id;

    private long totalPrice;

    private int sizeVn;

    private double sizeUs;

    private double sizeCm;

    private String productName;
    private int status;
    private List productImg;

    public OrderInfoDTO(long id, long totalPrice, int sizeVn, String productName, List productImg) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.sizeVn = sizeVn;
        this.productName = productName;
        this.productImg = productImg;
    }

    public OrderInfoDTO(Order od) {
        this.id = od.getId();
        this.totalPrice = od.getTotalPrice();
        this.status = od.getStatus();
        this.productName = od.getProduct().getName();
        this.sizeVn = od.getSize();
        this.productImg = od.getProduct().getImages();
    }
}