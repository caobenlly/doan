package com.example.doantotnghiep.responsitory;

import com.example.doantotnghiep.entity.Category;
import com.example.doantotnghiep.entity.ProductCategory;
import com.example.doantotnghiep.model.responeadmin.ProductsAdminResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProductCategoryReponsitory extends JpaRepository<ProductCategory,String> {

    @Query(value = "SELECT NEW ProductCategory(p.id,p.categoryId   ) FROM ProductCategory p where p.id =:product_id")
    List<ProductCategory> adminGetListCategory(@Param("product_id") String id);


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "Delete from product_category where product_id = ?1")
    public void deleteByProductId(String id);
}
