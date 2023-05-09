package com.example.doantotnghiep.controller.shop;


import com.example.doantotnghiep.entity.*;
import com.example.doantotnghiep.entity.exception.BadRequestException;
import com.example.doantotnghiep.entity.exception.NotFoundException;
import com.example.doantotnghiep.model.dto.CheckPromotion;
import com.example.doantotnghiep.model.dto.DetailProductInfoDTO;
import com.example.doantotnghiep.model.dto.PageableDTO;
import com.example.doantotnghiep.model.dto.ProductInfoDTO;
import com.example.doantotnghiep.model.request.CreateOrderRequest;
import com.example.doantotnghiep.model.request.FilterProductRequest;
import com.example.doantotnghiep.model.responeadmin.CommentResponse;
import com.example.doantotnghiep.model.responeadmin.ListSanPhamHome;
import com.example.doantotnghiep.model.responeadmin.ProductsAdminResponse;
import com.example.doantotnghiep.responsitory.*;
import com.example.doantotnghiep.security.CustomUserDetails;
import com.example.doantotnghiep.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

import static com.example.doantotnghiep.config.Contant.*;

@CrossOrigin("*")
@RestController
@RequestMapping("home")
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private PostService postService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PromotionService promotionService;
    @Autowired
    private ProductRepository repository;
    @Autowired
    private ProductSizeRepository productSizeRepository;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ProductCategoryReponsitory productCategoryReponsitory;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;
    @GetMapping("/shop/index")
    public ResponseEntity<?>  homePage(Model model){

        //Lấy 5 sản phẩm mới nhất
        List<ProductInfoDTO> newProducts = productService.getListNewProducts();
        model.addAttribute("newProducts", newProducts);

        //Lấy 5 sản phẩm bán chạy nhất
        List<ProductInfoDTO> bestSellerProducts = productService.getListBestSellProducts();
        model.addAttribute("bestSellerProducts", bestSellerProducts);

        //Lấy 5 sản phẩm có lượt xem nhiều
        List<ProductInfoDTO> viewProducts = productService.getListViewProducts();
        model.addAttribute("viewProducts", viewProducts);

        //Lấy danh sách nhãn hiệu
        List<Brand> brands = brandService.getListBrand();
        model.addAttribute("brands",brands);

        //Lấy 5 bài viết mới nhất
        List<Post> posts = postService.getLatesPost();
        model.addAttribute("posts", posts);

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>  getProductDetail(Model model, @PathVariable String id){

        //Lấy thông tin sản phẩm
        DetailProductInfoDTO product;
        try {
            product = productService.getDetailProductById(id);
        } catch (NotFoundException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        model.addAttribute("product", product);

        //Lấy sản phẩm liên quan
        List<ProductInfoDTO> relatedProducts = productService.getRelatedProducts(id);
        model.addAttribute("relatedProducts", relatedProducts);

        // Lấy size có sẵn
        List<Integer> availableSizes = productService.getListAvailableSize(id);
        model.addAttribute("availableSizes", availableSizes);
        if (!availableSizes.isEmpty()) {
            model.addAttribute("canBuy", true);
        } else {
            model.addAttribute("canBuy", false);
        }

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @GetMapping("/dat-hang/shop/payment")
    public ResponseEntity<?> getCartPage(Model model, @RequestParam String id,@RequestParam int size){

        //Lấy chi tiết sản phẩm
        DetailProductInfoDTO product;
        try {
            product = productService.getDetailProductById(id);
        } catch (NotFoundException ex) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        model.addAttribute("product", product);

        //Validate size
        if (size < 35 || size > 42) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        //Lấy danh sách size có sẵn
        List<Integer> availableSizes = productService.getListAvailableSize(id);
        model.addAttribute("availableSizes", availableSizes);
        boolean notFoundSize = true;
        for (Integer availableSize : availableSizes) {
            if (availableSize == size) {
                notFoundSize = false;
                break;
            }
        }
        model.addAttribute("notFoundSize", notFoundSize);

        //Lấy danh sách size
        model.addAttribute("sizeVn", SIZE_VN);
        model.addAttribute("sizeUs", SIZE_US);
        model.addAttribute("sizeCm", SIZE_CM);
        model.addAttribute("size", size);

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @PostMapping("/api/orders")
    public ResponseEntity<Object> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Order order = orderService.createOrder(createOrderRequest, user.getId());

        return ResponseEntity.ok(order.getId());
    }

    @GetMapping("/products")
    public ResponseEntity<Object> getListBestSellProducts(){
        List<ProductInfoDTO> productInfoDTOS = productService.getListBestSellProducts();
        return ResponseEntity.ok(productInfoDTOS);
    }

    @GetMapping("/san-pham/shop/product")
    public ResponseEntity<?> getProductShopPages(Model model){

        //Lấy danh sách nhãn hiệu
        List<Brand> brands = brandService.getListBrand();
        model.addAttribute("brands",brands);


        //Lấy danh sách danh mục
        List<Category> categories = categoryService.getListCategories();
        model.addAttribute("categories",categories);
        //Danh sách size của sản phẩm
        model.addAttribute("sizeVn", SIZE_VN);

        //Lấy danh sách sản phẩm


        List<ListSanPhamHome> result = repository.getAllProduct();
        for (ListSanPhamHome tr : result){
           List<ProductSize> productSize = productSizeRepository.findByProductId(tr.getId());
            List<Integer> size = new ArrayList<>();
           for (ProductSize productSize1 : productSize){
               size.add(productSize1.getSize());
           }
           tr.setSize(size);
        }
        for (ListSanPhamHome tr : result){
            List<ProductCategory> ct = productCategoryReponsitory.adminGetListCategory(tr.getId());

            List<String> category = new ArrayList<>();
            for (ProductCategory a : ct){
                Optional<Category> categories1 = categoryRepository.findById(a.getCategoryId());

                    category.add(categories1.get().getName());

            }
            tr.setCategoryName(category);

        }

        model.addAttribute("Product", result);


        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @PostMapping("/api/san-pham/loc")
    public ResponseEntity<?> filterProduct(@RequestBody FilterProductRequest req) {
        // Validate
        if (req.getMinPrice() == null) {
            req.setMinPrice((long) 0);
        } else {
            if (req.getMinPrice() < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mức giá phải lớn hơn 0");
            }
        }
        if (req.getMaxPrice() == null) {
            req.setMaxPrice(Long.MAX_VALUE);
        } else {
            if (req.getMaxPrice() < 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mức giá phải lớn hơn 0");
            }
        }

        PageableDTO result = productService.filterProduct(req);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/tim-kiem/shop/search")
    public ResponseEntity<?> searchProduct(Model model, @RequestParam(required = false) String keyword, @RequestParam(required = false) Integer page) {

        PageableDTO result = productService.searchProductByKeyword(keyword, page);

        model.addAttribute("totalPages", result.getTotalPages());
        model.addAttribute("currentPage", result.getCurrentPage());
        model.addAttribute("listProduct", result.getItems());
        model.addAttribute("keyword", keyword);
        if (((List<?>) result.getItems()).isEmpty()) {
            model.addAttribute("hasResult", false);
        } else {
            model.addAttribute("hasResult", true);
        }

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @GetMapping("/api/check-hidden-promotion")
    public ResponseEntity<Object> checkPromotion(@RequestParam String code) {
        if (code == null || code == "") {
            throw new BadRequestException("Mã code trống");
        }

        Promotion promotion = promotionService.checkPromotion(code);
        if (promotion == null) {
            throw new BadRequestException("Mã code không hợp lệ");
        }
        CheckPromotion checkPromotion = new CheckPromotion();
        checkPromotion.setDiscountType(promotion.getDiscountType());
        checkPromotion.setDiscountValue(promotion.getDiscountValue());
        checkPromotion.setMaximumDiscountValue(promotion.getMaximumDiscountValue());
        return ResponseEntity.ok(checkPromotion);
    }

    @GetMapping("lien-he")
    public String contact(){
        return "shop/lien-he";
    }
    @GetMapping("huong-dan")
    public String buyGuide(){
        return "shop/buy-guide";
    }
    @GetMapping("doi-hang")
    public String doiHang(){
        return "shop/doi-hang";
    }

    @GetMapping("/listcomment/{id}")
    public ResponseEntity<Object> getListComment(@PathVariable String id, Model model){
        List<Comment> productInfoDTOS = commentRepository.findCommentByProductId(id) ;

        List<String> rp = new ArrayList<>();
//        List<CommentResponse> productComment = new ArrayList<>();
        Map<String,String> it = new HashMap<>();
        for (Comment comment : productInfoDTOS) {
            Optional<User> user = userRepository.findById(comment.getUser().getId());
            List<CommentResponse> productComment = commentRepository.getListComment(id) ;

            if (user.get().getFullName() == null){
                throw new BadRequestException("user để trống tên"+user.get().getEmail());
            }
            model.addAttribute("Listcomment",productComment);
        }

        return ResponseEntity.ok(model);
    }


    @GetMapping("/listfeedback/{id}")
    public ResponseEntity<Object> getListFeeback(@PathVariable String id) {
        Optional<Product> productsAdminResponse = repository.findById(id);
        List<String> img = new ArrayList<>();
        if (productsAdminResponse.isPresent()) {
            img = productsAdminResponse.get().getImageFeedBack();
        }
        return ResponseEntity.ok(img);
    }

}
