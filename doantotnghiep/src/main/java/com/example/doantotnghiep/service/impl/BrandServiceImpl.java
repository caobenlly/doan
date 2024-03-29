package com.example.doantotnghiep.service.impl;


import com.example.doantotnghiep.entity.Brand;
import com.example.doantotnghiep.entity.exception.BadRequestException;
import com.example.doantotnghiep.entity.exception.InternalServerException;
import com.example.doantotnghiep.entity.exception.NotFoundException;
import com.example.doantotnghiep.model.mapper.BrandMapper;
import com.example.doantotnghiep.model.request.CreateBrandRequest;
import com.example.doantotnghiep.responsitory.BrandRepository;
import com.example.doantotnghiep.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


@Component
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;

    @Override
    public  List<Brand> adminGetListBrands(String id, String name, String status) {
        return brandRepository.adminGetListBrands(id, name, status);

    }

    @Override
    public List<Brand> getListBrand() {
        return brandRepository.findAll();
    }

    @Override
    public Brand createBrand(CreateBrandRequest createBrandRequest) {
        Brand brand = brandRepository.findByName(createBrandRequest.getName());
        if (brand != null) {
            throw new BadRequestException("Tên nhãn hiệu đã tồn tại trong hệ thống, Vui lòng chọn tên khác!");
        }
        if (createBrandRequest.getThumbnail() == null && createBrandRequest.isStatus()==true) {
            throw new BadRequestException("Vui lòng thêm ảnh!");
        }
        brand = BrandMapper.toBrand(createBrandRequest);
        brandRepository.save(brand);
        return brand;
    }

    @Override
    public void updateBrand(CreateBrandRequest createBrandRequest, Long id) {
        Optional<Brand> brand = brandRepository.findById(id);
        if (brand.equals(null)) {
            throw new NotFoundException("Tên nhãn hiệu không tồn tại!");
        }
//        Brand brand1 = brandRepository.findByName(createBrandRequest.getName());
//        if (brand1 != null) {
//            throw new BadRequestException("Tên nhãn hiệu đã tồn tại trong hệ thống, Vui lòng chọn tên khác!");
//        }
        Brand rs = brand.get();
        rs.setId(id);
        rs.setName(createBrandRequest.getName());
        rs.setDescription(createBrandRequest.getDescription());
        rs.setThumbnail(createBrandRequest.getThumbnail());
        rs.setStatus(createBrandRequest.isStatus());
        rs.setModifiedAt(new Timestamp(System.currentTimeMillis()));

        try {
            brandRepository.save(rs);
        } catch (Exception ex) {
            throw new InternalServerException("Tên nhãn hiệu đã tồn tại");
        }
    }

    @Override
    public void deleteBrand(long id) {
        Optional<Brand> brand = brandRepository.findById(id);
        if (brand.equals(null)) {
            throw new NotFoundException("Tên nhãn hiệu không tồn tại!");
        }
        try {
            brandRepository.deleteById(id);
        } catch (Exception ex) {
            throw new InternalServerException("Lỗi khi xóa nhãn hiệu!");
        }
    }

    @Override
    public Brand getBrandById(long id) {
        Optional<Brand> brand = brandRepository.findById(id);
        if (brand.equals(null)) {
            throw new NotFoundException("Tên nhãn hiệu không tồn tại!");
        }
        return brand.get();
    }

    @Override
    public long getCountBrands() {
        return brandRepository.count();
    }
}
