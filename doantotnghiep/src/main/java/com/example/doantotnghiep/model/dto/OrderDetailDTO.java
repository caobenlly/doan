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
public class OrderDetailDTO {
    private long id;

    private long totalPrice;


    private long productPrice;

    private String receiverName;

    private String receiverPhone;

    private String receiverAddress;

    private int status;

    private String statusText;

    private int sizeVn;

    private double sizeUs;

    private double sizeCm;

    private String productName;

    private List productImg;

    public OrderDetailDTO (long id, long totalPrice, long productPrice, String receiverName, String receiverPhone, String receiverAddress, int status, int sizeVn, String productName, List productImg) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.productPrice = productPrice;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverAddress = receiverAddress;
        this.status = status;
        this.sizeVn = sizeVn;
        this.productName = productName;
        this.productImg = productImg;
    }

    public OrderDetailDTO(Order od) {
        this.id = od.getId();
        this.totalPrice = od.getTotalPrice();
        this.productPrice = od.getProduct().getPrice();
        this.receiverName = od.getReceiverName();
        this.receiverPhone = od.getReceiverPhone();
        this.receiverAddress = od.getReceiverAddress();
        this.status = od.getStatus();
        this.productName = od.getProduct().getName();
        this.sizeVn = od.getSize();
        this.productImg = od.getProduct().getImages() ;
    }
}
