package com.example.doantotnghiep.controller.admin;

import com.example.doantotnghiep.entity.Order;
import com.example.doantotnghiep.entity.Promotion;
import com.example.doantotnghiep.entity.User;
import com.example.doantotnghiep.entity.exception.BadRequestException;
import com.example.doantotnghiep.model.dto.OrderDetailDTO;
import com.example.doantotnghiep.model.dto.OrderInfoDTO;
import com.example.doantotnghiep.model.dto.ShortProductInfoDTO;
import com.example.doantotnghiep.model.request.CreateOrderRequest;
import com.example.doantotnghiep.model.request.UpdateDetailOrder;
import com.example.doantotnghiep.model.request.UpdateStatusOrderRequest;
import com.example.doantotnghiep.model.responeadmin.OrdersAdminResponse;
import com.example.doantotnghiep.responsitory.OrderRepository;
import com.example.doantotnghiep.security.CustomUserDetails;
import com.example.doantotnghiep.service.OrderService;
import com.example.doantotnghiep.service.ProductService;
import com.example.doantotnghiep.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.example.doantotnghiep.config.Contant.*;

@RestController
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/admin/orders")
    public ResponseEntity<Object> getListOrderPage(Model model,
                                   @RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "", required = false) String id,
                                   @RequestParam(defaultValue = "", required = false) String name,
                                   @RequestParam(defaultValue = "", required = false) String phone,
                                   @RequestParam(defaultValue = "", required = false) String status,
                                   @RequestParam(defaultValue = "", required = false) String product) {

        //Lấy danh sách sản phẩm
        List<ShortProductInfoDTO> productList = productService.getListProduct();
        model.addAttribute("productList", productList);


        //Lấy danh sách đơn hàng
        List<OrdersAdminResponse> orderPage = orderService.adminGetListOrders(page);
//        model.addAttribute("orderPage", orderPage.getContent());
//        model.addAttribute("totalPages", orderPage.getTotalPages());
//        model.addAttribute("currentPage", orderPage.getPageable().getPageNumber() + 1);

        return ResponseEntity.ok(orderPage);
    }

    @GetMapping("/admin/orders/create")
    public ResponseEntity<Object> createOrderPage(Model model) {

        //Get list product
        List<ShortProductInfoDTO> products = productService.getAvailableProducts();
        model.addAttribute("products", products);

        // Get list size
        model.addAttribute("sizeVn", SIZE_VN);

//        //Get list valid promotion
        List<Promotion> promotions = promotionService.getAllValidPromotion();
        model.addAttribute("promotions", promotions);
        return ResponseEntity.ok(model);
    }

    @PostMapping("/api/admin/orders")
    public ResponseEntity<Object> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Order order = orderService.createOrder(createOrderRequest, user.getId());
        return ResponseEntity.ok(order);
    }

    @GetMapping("/admin/orders/update/{id}")
    public ResponseEntity<Object> updateOrderPage(Model model, @PathVariable long id) {

        Order order = orderService.findOrderById(id);
        model.addAttribute("order", order);

        if (order.getStatus() == ORDER_STATUS) {
            // Get list product to select
            List<ShortProductInfoDTO> products = productService.getAvailableProducts();
            model.addAttribute("products", products);

            // Get list valid promotion
            List<Promotion> promotions = promotionService.getAllValidPromotion();
            model.addAttribute("promotions", promotions);
            if (order.getPromotion() != null) {
                boolean validPromotion = false;
                for (Promotion promotion : promotions) {
                    if (promotion.getCouponCode().equals(order.getPromotion().getCouponCode())) {
                        validPromotion = true;
                        break;
                    }
                }
                if (!validPromotion) {
                    promotions.add(new Promotion(order.getPromotion()));
                }
            }

            // Check size available
            boolean sizeIsAvailable = productService.checkProductSizeAvailable(order.getProduct().getId(), order.getSize());
            model.addAttribute("sizeIsAvailable", sizeIsAvailable);
        }

        return ResponseEntity.ok(model);
    }

    @PutMapping("/api/admin/orders/update-detail/{id}")
    public ResponseEntity<Object> updateDetailOrder(@RequestParam int trangThai,@PathVariable long id) {
        Optional<Order> order = orderRepository.findById(id);
        if(!order.isPresent()){
            throw new BadRequestException("Không có đơn hàng");
        }
        order.get().setStatus(trangThai);
        orderRepository.save(order.get());

        return ResponseEntity.ok("Cập nhật thành công");
    }

    @PutMapping("/api/admin/orders/update-status/{id}")
    public ResponseEntity<Object> updateStatusOrder(@Valid @RequestBody UpdateStatusOrderRequest updateStatusOrderRequest, @PathVariable long id) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        orderService.updateStatusOrder(updateStatusOrderRequest, id, user.getId());
        return ResponseEntity.ok("Cập nhật trạng thái thành công");
    }

    @GetMapping("/tai-khoan/lich-su-giao-dich")
    public ResponseEntity<Object> getOrderHistoryPage(Model model){

        //Get list order pending
        User user =((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        List<OrderInfoDTO> orderList = orderService.getListOrderOfPersonByStatus( user.getId());
        model.addAttribute("orderList",orderList);

        return ResponseEntity.ok(model);
    }

    @GetMapping("/api/get-order-list")
    public ResponseEntity<Object> getListOrderByStatus(@RequestParam int status) {
        // Validate status
        if (!LIST_ORDER_STATUS.contains(status)) {
            throw new BadRequestException("Trạng thái đơn hàng không hợp lệ");
        }

        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        List<OrderInfoDTO> orders = orderService.getListOrderOfPersonByStatus( user.getId());

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/tai-khoan/lich-su-giao-dich/{id}")
    public ResponseEntity<Object> getDetailOrderPage(Model model, @PathVariable int id) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        OrderDetailDTO order = orderService.userGetDetailById(id);
        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không có đơn hàng");
        }
        model.addAttribute("order", order);

        if (order.getStatus() == ORDER_STATUS) {
            model.addAttribute("canCancel", true);
        } else {
            model.addAttribute("canCancel", false);
        }

        return ResponseEntity.ok(model);
    }

    @PostMapping("/api/cancel-order/{id}")
    public ResponseEntity<Object> cancelOrder(@PathVariable long id) {
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        orderService.userCancelOrder(id, user.getId());

        return ResponseEntity.ok("Hủy đơn hàng thành công");
    }


    @DeleteMapping("/admin/orders/delete/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable long id) {

        orderRepository.deleteById(id);
        return ResponseEntity.ok("Xóa đơn hàng thành công");
    }
}
