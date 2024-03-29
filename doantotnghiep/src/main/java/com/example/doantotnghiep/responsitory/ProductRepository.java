package com.example.doantotnghiep.responsitory;

import com.example.doantotnghiep.entity.Product;
import com.example.doantotnghiep.model.dto.ChartDTO;
import com.example.doantotnghiep.model.dto.ProductInfoDTO;
import com.example.doantotnghiep.model.dto.ShortProductInfoDTO;
import com.example.doantotnghiep.model.responeadmin.ListSanPhamHome;
import com.example.doantotnghiep.model.responeadmin.ProductsAdminResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    //Lấy sản phẩm theo tên
    Product findByName(String name);

    //Lấy tất cả sản phẩm
//    @Query(value = "SELECT * FROM product pro right join (SELECT DISTINCT p.* FROM product p " +
//            "INNER JOIN product_category pc ON p.id = pc.product_id " +
//            "INNER JOIN category c ON c.id = pc.category_id " +
//            "WHERE p.id LIKE CONCAT('%',?1,'%') " +
//            "AND p.name LIKE CONCAT('%',?2,'%') " +
//            "AND c.id LIKE CONCAT('%',?3,'%') " +
//            "AND p.brand_id LIKE CONCAT('%',?4,'%')) as tb1 on pro.id=tb1.id", nativeQuery = true)

    @Query(value = " SELECT NEW com.example.doantotnghiep.model.responeadmin.ProductsAdminResponse(a,b,d) FROM Product a INNER JOIN Brand b on a.brand.id = b.id INNER JOIN ProductCategory c on a.id = c.id INNER JOIN Category d on c.categoryId = d.id")
    List<ProductsAdminResponse> adminGetListProducts();

    @Query(value = "SELECT NEW com.example.doantotnghiep.model.dto.ProductInfoDTO(p) FROM Product p WHERE p.status = 1 ORDER BY p.totalSold DESC ")
    List<ProductInfoDTO> getListBestSellProducts();

//    //Lấy sản phẩm được bán nhiều
//    @Query(nativeQuery = true,name = "getListBestSellProducts")
//    List<ProductInfoDTO> getListBestSellProducts1(int limit);

    //Lấy sản phẩm mới nhất
    @Query(value = "SELECT NEW com.example.doantotnghiep.model.dto.ProductInfoDTO(p) FROM Product p WHERE p.status = 1 ORDER BY p.createdAt DESC ")
    List<ProductInfoDTO> getListNewProducts();

    //Lấy sản phẩm được xem nhiều
    @Query(value = "SELECT NEW com.example.doantotnghiep.model.dto.ProductInfoDTO(p) FROM Product p WHERE p.status = 1 ORDER BY p.view DESC ")
    List<ProductInfoDTO> getListViewProducts();

    //Lấy sản phẩm liên quan
    @Query(value = "SELECT NEW com.example.doantotnghiep.model.dto.ProductInfoDTO(p) FROM Product p WHERE p.status = 1 AND p.id NOT IN(:id)")
    List<ProductInfoDTO> getRelatedProducts(@Param("id") String id);

    //Lấy sản phẩm
    @Query(name = "getAllProduct", nativeQuery = true)
    List<ShortProductInfoDTO> getListProduct();

    //Lấy sản phẩm có sẵn size
    @Query(nativeQuery = true, name = "getAllBySizeAvailable")
    List<ShortProductInfoDTO> getAvailableProducts();

    //Trừ một sản phẩm đã bán
    @Transactional
    @Modifying
    @Query(value = "UPDATE product SET total_sold = total_sold - 1 WHERE id = ?1", nativeQuery = true)
    void minusOneProductTotalSold(String productId);

    //Cộng một sản phẩm đã bán
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "Update product set total_sold = total_sold + 1 where id = ?1")
    void plusOneProductTotalSold(String productId);

    //Tìm kiến sản phẩm theo size
    @Query(nativeQuery = true, name = "searchProductBySize")
    List<ProductInfoDTO> searchProductBySize(List<Long> brands, List<Long> categories, long minPrice, long maxPrice, List<Integer> sizes, int limit, int offset);

    //Đếm số sản phẩm
    @Query(nativeQuery = true, value = "SELECT COUNT(DISTINCT d.id) " +
            "FROM (" +
            "SELECT DISTINCT product.id " +
            "FROM product " +
            "INNER JOIN product_category " +
            "ON product.id = product_category.product_id " +
            "WHERE product.status = 1 AND product.brand_id IN (?1) AND product_category.category_id IN (?2) " +
            "AND product.price > ?3 AND product.price < ?4) as d " +
            "INNER JOIN product_size " +
            "ON product_size.product_id = d.id " +
            "WHERE product_size.size IN (?5)")
    int countProductBySize(List<Long> brands, List<Long> categories, long minPrice, long maxPrice, List<Integer> sizes);

    //Tìm kiến sản phẩm k theo size
    @Query(nativeQuery = true, name = "searchProductAllSize")
    List<ProductInfoDTO> searchProductAllSize(List<Long> brands, List<Long> categories, long minPrice, long maxPrice, int limit, int offset);

    //Đếm số sản phẩm
    @Query(nativeQuery = true, value = "SELECT COUNT(DISTINCT product.id) " +
            "FROM product " +
            "INNER JOIN product_category " +
            "ON product.id = product_category.product_id " +
            "WHERE product.status = 1 AND product.brand_id IN (?1) AND product_category.category_id IN (?2) " +
            "AND product.price > ?3 AND product.price < ?4 ")
    int countProductAllSize(List<Long> brands, List<Long> categories, long minPrice, long maxPrice);

    //Tìm kiến sản phẩm theo tên và tên danh mục
    @Query(nativeQuery = true, name = "searchProductByKeyword")
    List<ProductInfoDTO> searchProductByKeyword(@Param("keyword") String keyword, @Param("limit") int limit, @Param("offset") int offset);

    //Đếm số sản phẩm
    @Query(nativeQuery = true, value = "SELECT count(DISTINCT product.id) " +
            "FROM product " +
            "INNER JOIN product_category " +
            "ON product.id = product_category.product_id " +
            "INNER JOIN category " +
            "ON category.id = product_category.category_id " +
            "WHERE product.status = true AND (product.name LIKE CONCAT('%',:keyword,'%') OR category.name LIKE CONCAT('%',:keyword,'%')) ")
    int countProductByKeyword(@Param("keyword") String keyword);

    @Query(name = "getProductOrders",nativeQuery = true)
    List<ChartDTO> getProductOrders(Integer moth, Integer year);

    //Lấy thông tin doanh số
    @Query(value = "SELECT DATE_FORMAT(b.created_at, '%Y-%m') AS thang, SUM(b.sale_price) AS tong_doanh_so FROM product b GROUP BY thang",nativeQuery = true)
    List<ChartDTO> getDoanhSo();


    //Lấy sản phẩm mới nhất
    @Query(value = "SELECT NEW com.example.doantotnghiep.model.responeadmin.ListSanPhamHome(a,b) FROM Product a INNER JOIN Brand b on a.brand.id = b.id  where a.status =1")
    List<ListSanPhamHome> getAllProduct();
}
