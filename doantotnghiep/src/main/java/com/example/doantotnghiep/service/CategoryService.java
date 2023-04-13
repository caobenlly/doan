package com.example.doantotnghiep.service;

import com.example.doantotnghiep.entity.Category;
import com.example.doantotnghiep.model.request.CreateCategoryRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<Category> getListCategories();

    Category getCategoryById(long id);

    Category createCategory(CreateCategoryRequest createCategoryRequest);

    void updateCategory(CreateCategoryRequest createCategoryRequest, long id);

    void deleteCategory(long id);

    List<Category> adminGetListCategory(String id, String name, String status);

    void updateOrderCategory(int[] ids);

    //Đếm số danh mục
    long getCountCategories();
}
