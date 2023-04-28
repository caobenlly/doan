package com.example.doantotnghiep.responsitory;

import com.example.doantotnghiep.entity.Order;
import com.example.doantotnghiep.model.dto.OrderDetailDTO;
import com.example.doantotnghiep.model.dto.OrderInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = "SELECT * FROM orders " +
            "WHERE id LIKE CONCAT('%',?1,'%') " +
            "AND receiver_name LIKE CONCAT('%',?2,'%') " +
            "AND receiver_phone LIKE CONCAT('%',?3,'%') " +
            "AND status LIKE CONCAT('%',?4,'%') " , nativeQuery = true)
//            "AND product_id LIKE CONCAT('%',?5,'%')", nativeQuery = true)
    Page<Order> adminGetListOrder(String id, String name, String phone, String status, String product, Pageable pageable);
    //    Page<Order> adminGetListOrder(String id, String name, String phone, String status, String product, Pageable pageable);
    @Query( value = "select new com.example.doantotnghiep.model.dto.OrderInfoDTO(o) from Order o where o.buyer.id= :id")
    List<OrderInfoDTO> getListOrderOfPersonByStatus( @Param("id") long  id);

    @Query( value = "select new com.example.doantotnghiep.model.dto.OrderDetailDTO(o) from Order o where o.id = :id")
    OrderDetailDTO userGetDetailById(@Param("id") long  id);

    @Query(value = "select count(product_id) AS A from orders where product_id = ?1;", nativeQuery = true)
    int countByProductIds(String id);

    int countByProductId(String id);
}
