package com.example.doantotnghiep.controller.admin;

import com.example.doantotnghiep.entity.Brand;
import com.example.doantotnghiep.entity.User;
import com.example.doantotnghiep.model.mapper.BrandMapper;
import com.example.doantotnghiep.model.request.CreateBrandRequest;
import com.example.doantotnghiep.security.CustomUserDetails;
import com.example.doantotnghiep.service.BrandService;
import com.example.doantotnghiep.service.ImageService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin("*")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/admin/brands")
    public ResponseEntity<Object> homePage(Model model,
                                      @RequestParam(defaultValue = "", required = false) String id,
                                      @RequestParam(defaultValue = "", required = false) String name,
                                      @RequestParam(defaultValue = "", required = false) String status,
                                      @RequestParam(defaultValue = "1", required = false) Integer page) {

        //Lấy tất cả các anh của user upload
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        List<String> images = imageService.getListImageOfUser(user.getId());
        model.addAttribute("images", images);

        List<Brand> brands = brandService.adminGetListBrands(id, name, status);
        model.addAttribute("brands", brands);
        return ResponseEntity.ok(model);
    }

    @PostMapping("/api/admin/brands")
    public ResponseEntity<Object> createBrand(@Valid @RequestBody CreateBrandRequest createBrandRequest) {
        Brand brand = brandService.createBrand(createBrandRequest);
        return ResponseEntity.ok("Thêm mới nhãn hiệu thành công");
    }

    @PutMapping("/api/admin/brands/{id}")
    public ResponseEntity<Object> updateBrand(@Valid @RequestBody CreateBrandRequest createBrandRequest, @PathVariable long id) {
        brandService.updateBrand(createBrandRequest, id);
        return ResponseEntity.ok("Sửa nhãn hiệu thành công!");
    }

    @DeleteMapping("/api/admin/brands/{id}")
    public ResponseEntity<Object> deleteBrand(@PathVariable long id) {
        brandService.deleteBrand(id);
        return ResponseEntity.ok("Xóa nhãn hiệu thành công!");
    }
    @GetMapping("/api/admin/brands/{id}")
    public ResponseEntity<Object> getBrandById(@PathVariable long id){
        Brand brand = brandService.getBrandById(id);
        return ResponseEntity.ok(brand);
    }
}
