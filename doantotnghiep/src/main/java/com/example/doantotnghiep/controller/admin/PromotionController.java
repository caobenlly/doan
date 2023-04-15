package com.example.doantotnghiep.controller.admin;

import com.example.doantotnghiep.entity.Promotion;
import com.example.doantotnghiep.model.request.CreatePromotionRequest;
import com.example.doantotnghiep.service.PromotionService;
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
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    @GetMapping("/admin/promotions")
    public ResponseEntity<Object> getListPromotionPages(Model model,
                                        @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "", required = false) String code,
                                        @RequestParam(defaultValue = "", required = false) String name,
                                        @RequestParam(defaultValue = "", required = false) String publish,
                                        @RequestParam(defaultValue = "", required = false) String active) {


        List<Promotion> promotionPage = promotionService.adminGetListPromotion(code, name, publish, active, page);

        return ResponseEntity.ok(promotionPage);
    }

    @GetMapping("/admin/promotions/create")
    public ResponseEntity<Object> createPromotionPage(Model model) {
        return ResponseEntity.ok(model);
    }

    @PostMapping("/api/admin/promotions")
    public ResponseEntity<Object> createPromotion(@Valid @RequestBody CreatePromotionRequest createPromotionRequest) {
        Promotion promotion = promotionService.createPromotion(createPromotionRequest);
        return ResponseEntity.ok(promotion.getId());
    }

    @GetMapping("/admin/promotions/update/{id}")
    public ResponseEntity<Object> updatePromotionPage(Model model, @PathVariable long id) {
        Promotion promotion = promotionService.findPromotionById(id);
        model.addAttribute("promotion", promotion);
        return ResponseEntity.ok(model);
    }

    @PutMapping("/api/admin/promotions/{id}")
    public ResponseEntity<Object> updatePromotion(@Valid @RequestBody CreatePromotionRequest createPromotionRequest, @PathVariable long id) {
        promotionService.updatePromotion(createPromotionRequest, id);
        return ResponseEntity.ok("Cập nhật thành công");
    }

    @DeleteMapping("/api/admin/promotions/{id}")
    public ResponseEntity<Object> deletePromotion(@PathVariable long id) {
        promotionService.deletePromotion(id);
        return ResponseEntity.ok("Xóa khuyến mại thành công");
    }
}
