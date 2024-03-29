package com.example.doantotnghiep.service;


import com.example.doantotnghiep.entity.Brand;
import com.example.doantotnghiep.model.request.CreateBrandRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BrandService {
    List<Brand> adminGetListBrands(String id, String name, String status);

    List<Brand> getListBrand();

    Brand createBrand(CreateBrandRequest createBrandRequest);

    void updateBrand(CreateBrandRequest createBrandRequest, Long id);

    void deleteBrand(long id);

    Brand getBrandById(long id);

    long getCountBrands();
}
