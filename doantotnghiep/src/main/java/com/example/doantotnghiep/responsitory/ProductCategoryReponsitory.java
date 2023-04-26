package com.example.doantotnghiep.responsitory;

import com.example.doantotnghiep.entity.Category;
import com.example.doantotnghiep.entity.ProductCategory;
import com.example.doantotnghiep.model.responeadmin.ProductsAdminResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ProductCategoryReponsitory extends JpaRepository<ProductCategory,String> {

    @Query(value = "SELECT NEW ProductCategory(p.id,p.categoryId   ) FROM ProductCategory p where p.id =:product_id")
    List<ProductCategory> adminGetListCategory(@Param("product_id") String id);

}
