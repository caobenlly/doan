package com.example.doantotnghiep.model.dto;

import com.example.doantotnghiep.entity.Product;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;


@Data
@NoArgsConstructor
public class ProductInfoDTO {
    private String id;
    private String name;
    private String slug;
    private long price;
    private int views;
    private ArrayList images;
    private long totalSold;
    private long promotionPrice;

    public ProductInfoDTO(Product p) {
        this.id = p.getId();
        this.name = p.getName();
        this.slug = p.getSlug();
        this.price = p.getPrice();
        this.views = p.getView();
        this.images = p.getImages();
        this.totalSold = p.getTotalSold();
    }
}