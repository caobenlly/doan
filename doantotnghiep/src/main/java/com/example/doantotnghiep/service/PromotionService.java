package com.example.doantotnghiep.service;

import com.example.doantotnghiep.entity.Promotion;
import com.example.doantotnghiep.model.request.CreatePromotionRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PromotionService {

    List<Promotion> adminGetListPromotion(String code, String name, String publish, String active);

    Promotion createPromotion(CreatePromotionRequest createPromotionRequest);

    void updatePromotion(CreatePromotionRequest createPromotionRequest, long id);

    void deletePromotion(long id);

    Promotion findPromotionById(long id);

    //Kiểm tra có khuyến mại
    Promotion checkPublicPromotion();

    //Tính giá sản phẩm khi có khuyến mại
    long calculatePromotionPrice(long price, Promotion promotion);

    //Lấy khuyến mại theo mã code
    Promotion checkPromotion(String code);

    //Lấy khuyến mại đang chạy và còn thời hạn
    List<Promotion> getAllValidPromotion();
}
