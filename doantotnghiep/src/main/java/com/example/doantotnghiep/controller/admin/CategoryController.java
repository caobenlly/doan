package com.example.doantotnghiep.controller.admin;

import com.example.doantotnghiep.entity.Category;
import com.example.doantotnghiep.model.mapper.CategoryMapper;
import com.example.doantotnghiep.model.request.CreateCategoryRequest;
import com.example.doantotnghiep.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@CrossOrigin("*")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/admin/categories")
    public ResponseEntity<Object> homePage(Model model,
                           @RequestParam(defaultValue = "",required = false) String id,
                           @RequestParam(defaultValue = "",required = false) String name,
                           @RequestParam(defaultValue = "",required = false) String status,
                           @RequestParam(defaultValue = "1",required = false) Integer page){

       List<Category> categories = categoryService.adminGetListCategory(id,name,status);
        return ResponseEntity.ok(categories);
    }


    @GetMapping("/api/admin/categories")
    public ResponseEntity<Object> adminGetListCategories(@RequestParam(defaultValue = "",required = false) String id,
                                                         @RequestParam(defaultValue = "",required = false) String name,
                                                         @RequestParam(defaultValue = "",required = false) String status,
                                                         @RequestParam(defaultValue = "0",required = false) Integer page){
        List<Category> categories = categoryService.adminGetListCategory(id,name,status);
        return ResponseEntity.ok(categories);

    }
    @GetMapping("/api/admin/categories/{id}")
    public ResponseEntity<Object> getCategoryById(@PathVariable long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(CategoryMapper.toCategoryDTO(category));
    }

    @PostMapping("/api/admin/categories")
    public ResponseEntity<Object> createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        Category category = categoryService.createCategory(createCategoryRequest);
        return ResponseEntity.ok(CategoryMapper.toCategoryDTO(category));
    }

    @PutMapping("/api/admin/categories/{id}")
    public ResponseEntity<Object> updateCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest, @PathVariable long id) {
        categoryService.updateCategory(createCategoryRequest, id);
        return ResponseEntity.ok("Sửa danh mục thành công!");
    }

    @DeleteMapping("/api/admin/categories/{id}")
    public ResponseEntity<Object> deleteCategory(@PathVariable long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Xóa danh mục thành công!");
    }

    @PutMapping("/api/admin/categories")
    public ResponseEntity<Object> updateOrderCategory(@RequestBody int[] ids){
        categoryService.updateOrderCategory(ids);
        return ResponseEntity.ok("Thay đổi thứ tự thành công!");
    }
}
