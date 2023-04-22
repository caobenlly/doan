package com.example.doantotnghiep.controller.admin;

import com.example.doantotnghiep.entity.Brand;
import com.example.doantotnghiep.entity.Category;
import com.example.doantotnghiep.entity.Product;
import com.example.doantotnghiep.model.dto.ChartDTO;
import com.example.doantotnghiep.model.dto.StatisticDTO;
import com.example.doantotnghiep.model.request.FilterDayByDay;
import com.example.doantotnghiep.model.responeadmin.ProductsAdminResponse;
import com.example.doantotnghiep.responsitory.*;
import com.example.doantotnghiep.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
public class DashboardController {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private PostService postService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private StatisticRepository statisticRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admin")
    public ResponseEntity<Object> dashboard(Model model){
        model.addAttribute("Post",postService.getCountPost());
        model.addAttribute("Product",productService.getCountProduct());
        model.addAttribute("Order",orderService.getCountOrder());
        model.addAttribute("Category",categoryService.getCountCategories());
        model.addAttribute("Brand",brandService.getCountBrands());
        return ResponseEntity.ok(model);
    }

    @GetMapping("/api/admin/count/posts")
    public ResponseEntity<Object> getCountPost(){
        long countPosts = postService.getCountPost();
        return ResponseEntity.ok(countPosts);
    }

    @GetMapping("/api/admin/count/products")
    public ResponseEntity<Object> getCountProduct(){
        long countProducts = productService.getCountProduct();
        return ResponseEntity.ok(countProducts);
    }

    @GetMapping("/api/admin/count/orders")
    public ResponseEntity<Object> getCountOrders(){
        long countOrders = orderService.getCountOrder();
        return ResponseEntity.ok(countOrders);
    }

    @GetMapping("/api/admin/count/categories")
    public ResponseEntity<Object> getCountCategories(){
        long countCategories = categoryService.getCountCategories();
        return ResponseEntity.ok(countCategories);
    }

    @GetMapping("/api/admin/count/brands")
    public ResponseEntity<Object> getCountBrands(){
        long countBrands = brandService.getCountBrands();
        return ResponseEntity.ok(countBrands);
    }

    @GetMapping("/api/admin/count/users")
    public ResponseEntity<Object> getCountUsers(){
        long countUsers = userRepository.count();
        return ResponseEntity.ok(countUsers);
    }

    @GetMapping("/api/admin/statistics")
    public ResponseEntity<Object> getStatistic30Day(){
        List<StatisticDTO> statistics = statisticRepository.getStatistic30Day();
        return ResponseEntity.ok(statistics);
    }

    @PostMapping("/api/admin/statistics")
    public ResponseEntity<Object> getStatisticDayByDay(@RequestBody FilterDayByDay filterDayByDay){
        List<StatisticDTO> statisticDTOS = statisticRepository.getStatisticDayByDay(filterDayByDay.getToDate(),filterDayByDay.getFromDate());
        return ResponseEntity.ok(statisticDTOS);
    }

    @GetMapping("/api/admin/product-order-categories")
    public ResponseEntity<Object> getListProductOrderCategories(){
        HashMap<String, Long> hashMap1 = new HashMap<String, Long>();
        List<ProductsAdminResponse> adminResponses = productRepository.adminGetListProducts();
        List<Category> categories = categoryRepository.findAll();
        long dem = 0;
        for(Category category : categories){
            for (ProductsAdminResponse tr : adminResponses){
                if(tr.getDanhMuc().equals(category.getName())){
                    dem += tr.getDaBan();
                }
            }
            hashMap1.put(category.getName(), dem);
            dem =0;
        }

        return ResponseEntity.ok(hashMap1);
    }

    @GetMapping("/api/admin/product-order-brands")
    public ResponseEntity<Object> getProductOrderBrands(){
        HashMap<String, Long> hashMap1 = new HashMap<String, Long>();
        List<Product> product = productRepository.findAll();
        List<Brand> brands = brandRepository.findAll();
        long dem = 0;
        for(Brand brand : brands){
            for (Product tr : product){
                if(tr.getBrand().getId().equals(brand.getId())){
                   dem += tr.getTotalSold();
                }
            }
            hashMap1.put(brand.getName(), dem);
            dem =0;
        }

        return ResponseEntity.ok(hashMap1);
    }

    @GetMapping("/api/admin/product-order")
    public ResponseEntity<Object> getProductOrder(){


            String sql = "SELECT DATE_FORMAT(b.created_at, '%Y-%m') AS thang, SUM(b.sale_price) AS tong_doanh_so FROM product b GROUP BY thang";
            Query query = entityManager.createNativeQuery(sql);
        return ResponseEntity.ok(query.getResultList());
    }
}
