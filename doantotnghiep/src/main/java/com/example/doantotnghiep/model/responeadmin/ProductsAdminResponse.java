package com.example.doantotnghiep.model.responeadmin;

import com.example.doantotnghiep.entity.Brand;
import com.example.doantotnghiep.entity.Category;
import com.example.doantotnghiep.entity.Product;
import com.example.doantotnghiep.entity.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;

@Data
@NoArgsConstructor
public class ProductsAdminResponse {


    public ProductsAdminResponse(Product product, Brand B, Category d) {
        this.maSanPham =  product.getId();
        this.tenSanPham =  product.getName();
        this.anhSanPham =  product.getImages();
        this.nhanHieu = B.getName();
        this.danhMuc = d.getName();
        this.giaNhap =  product.getPrice();
        this.giaBan =  product.getSalePrice();
        this.ngayTao =  product.getCreatedAt();
        this.ngaySua =  product.getModifiedAt() ;
        this.daBan = product.getTotalSold();
    }

    private String maSanPham;
    private String tenSanPham;
    private ArrayList anhSanPham;
    private String nhanHieu;
    private String danhMuc;
    private long giaNhap;
    private long giaBan;
    private Timestamp ngayTao;
    private Timestamp ngaySua;
    private long daBan;

}
