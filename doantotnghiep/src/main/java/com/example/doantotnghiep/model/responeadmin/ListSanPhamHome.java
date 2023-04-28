package com.example.doantotnghiep.model.responeadmin;

import com.example.doantotnghiep.entity.Brand;
import com.example.doantotnghiep.entity.Category;
import com.example.doantotnghiep.entity.Product;
import com.example.doantotnghiep.entity.ProductSize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListSanPhamHome {

    private String id;
    private String name;
    private String slug;
    private long price;
    private int views;
    private ArrayList images;
    private long totalSold;
    private long promotionPrice;
    private String brandName;
    private List<String> categoryName;
    private List<Integer> size;

    public ListSanPhamHome(Product p, Brand b) {
        this.id = p.getId();
        this.name = p.getName();
        this.slug = p.getSlug();
        this.price = p.getPrice();
        this.views = p.getView();
        this.images = p.getImages();
        this.totalSold = p.getTotalSold();
        this.brandName = b.getName();

    }
}
