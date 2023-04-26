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
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductsAdminResponse {


    public ProductsAdminResponse(Product product, Brand B, Category d) {

        this.maSanPham =  product.getId();
        this.tenSanPham =  product.getName();
        this.anhSanPham =  product.getImages();
        this.nhanHieu = B.getName();
        this.danhMuc = Collections.singletonList(d.getName());
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
    private List danhMuc;
    private long giaNhap;
    private String description;
    private long giaBan;
    private Timestamp ngayTao;
    private Timestamp ngaySua;
    private long daBan;
    private int status;
    private Long nhanHieuId;

    public ProductsAdminResponse(Product p) {
        this.maSanPham = p.getId();
        this.tenSanPham = p.getName();
        this.anhSanPham = p.getImages();
        this.nhanHieuId = p.getBrand().getId();
        this.giaNhap = p.getPrice();
        this.giaBan = p.getSalePrice();
        this.ngayTao = p.getCreatedAt();
        this.ngaySua = p.getModifiedAt();
        this.daBan = p.getTotalSold();
        this.description = p.getDescription();
        this.status = p.getStatus();
    }

}
