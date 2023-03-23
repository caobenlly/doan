package com.example.doantotnghiep.service;

import com.example.doantotnghiep.entity.Order;
import com.example.doantotnghiep.model.dto.OrderDetailDTO;
import com.example.doantotnghiep.model.dto.OrderInfoDTO;
import com.example.doantotnghiep.model.request.CreateOrderRequest;
import com.example.doantotnghiep.model.request.UpdateDetailOrder;
import com.example.doantotnghiep.model.request.UpdateStatusOrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    Page<Order> adminGetListOrders(String id, String name, String phone, String status, String product, int page);

    Order createOrder(CreateOrderRequest createOrderRequest, int userId);

    void updateDetailOrder(UpdateDetailOrder updateDetailOrder, long id, int userId);

    Order findOrderById(long id);

    void updateStatusOrder(UpdateStatusOrderRequest updateStatusOrderRequest, long orderId, int userId);

    List<OrderInfoDTO> getListOrderOfPersonByStatus(int status, long userId);

    OrderDetailDTO userGetDetailById(long id, long userId);

    void userCancelOrder(long id, long userId);

    //Đếm số lượng đơn hàng
    long getCountOrder();

}
