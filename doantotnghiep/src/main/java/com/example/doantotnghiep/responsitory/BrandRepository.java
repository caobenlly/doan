package com.example.doantotnghiep.responsitory;


import com.example.doantotnghiep.entity.Brand;
import com.example.doantotnghiep.model.dto.ChartDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Brand findByName(String name);

    @Query(value = "SELECT * FROM brand b " +
            "WHERE b.id LIKE CONCAT('%',?1,'%') " +
            "AND b.name LIKE CONCAT('%',?2,'%') " +
            "AND b.status LIKE CONCAT('%',?3,'%')", nativeQuery = true)
    List<Brand> adminGetListBrands(String id, String name, String status);

    @Query(name = "getProductOrderBrands",nativeQuery = true)
    List<ChartDTO> getProductOrderBrands();

}
