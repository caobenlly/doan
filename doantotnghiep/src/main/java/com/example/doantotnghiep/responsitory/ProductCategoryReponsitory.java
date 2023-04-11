package com.example.doantotnghiep.responsitory;

import com.example.doantotnghiep.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProductCategoryReponsitory extends JpaRepository<ProductCategory,String> {



}
