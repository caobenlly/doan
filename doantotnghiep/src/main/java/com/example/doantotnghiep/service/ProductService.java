package com.example.doantotnghiep.service;


import com.example.doantotnghiep.entity.Product;
import com.example.doantotnghiep.entity.ProductSize;
import com.example.doantotnghiep.entity.Promotion;
import com.example.doantotnghiep.model.dto.DetailProductInfoDTO;
import com.example.doantotnghiep.model.dto.PageableDTO;
import com.example.doantotnghiep.model.dto.ProductInfoDTO;
import com.example.doantotnghiep.model.dto.ShortProductInfoDTO;
import com.example.doantotnghiep.model.request.CreateProductRequest;
import com.example.doantotnghiep.model.request.CreateSizeCountRequest;
import com.example.doantotnghiep.model.request.FilterProductRequest;
import com.example.doantotnghiep.model.request.UpdateFeedBackRequest;
import com.example.doantotnghiep.model.responeadmin.ProductsAdminResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {

    //Lấy sản phẩm
    List<ProductsAdminResponse> adminGetListProduct();

    //Tạo sản phẩm
    Product createProduct(CreateProductRequest createProductRequest);

    //Sửa sản phẩm
    void updateProduct(CreateProductRequest createProductRequest, String id);

//    Product createProduct(CreateProductRequest createProductRequest);

    //Lấy chi tiết sản phẩm
    ProductsAdminResponse getProductById(String id);

    //Xóa sản phẩm theo id
    void deleteProduct(String[] ids);

    //Xóa sản phẩm theo id
    void deleteProductById(String id);

    //Lấy sản phẩm bán nhiều nhất
    List<ProductInfoDTO> getListBestSellProducts();

    //Lấy sản phẩm mới nhất
    List<ProductInfoDTO> getListNewProducts();

    //Lấy sản phẩm xem nhiều
    List<ProductInfoDTO> getListViewProducts();

    //Lấy chi tiết sản phẩm theo id
    DetailProductInfoDTO getDetailProductById(String id);

    //Lấy sản phẩm liên quan
    List<ProductInfoDTO> getRelatedProducts(String id);

    //Lấy size có sẵn
    List<Integer> getListAvailableSize(String id);

//    //Nhập số lượng theo size

    void createSizeCount(CreateSizeCountRequest createSizeCountRequest);

    //Lấy size của sản phẩm
    List<ProductSize> getListSizeOfProduct(String id);

    List<ShortProductInfoDTO> getListProduct();

    //Lấy sản phẩm có sẵn size
    List<ShortProductInfoDTO> getAvailableProducts();

    //Check size sản phẩm
    boolean checkProductSizeAvailable(String id, int size);

    //Kiểm tra sản phẩm có khuyến mại
    List<ProductInfoDTO> checkPublicPromotion(List<ProductInfoDTO> products);

    //Tìm kiếm sản phẩm theo danh mục, nhãn hiệu, giá
    PageableDTO filterProduct(FilterProductRequest req);

//    PageableDTO filterProduct(FilterProductRequest req);

    //Tìm kiếm sản phẩm theo tên sản phẩm
    PageableDTO searchProductByKeyword(String keyword, Integer page);

    //Kiểm tra khuyến mại
    Promotion checkPromotion(String code);

    //Đếm số lượng sản phẩm
    long getCountProduct();

//    //Thêm ảnh feedBack

      void updatefeedBackImages(String id, UpdateFeedBackRequest req);

    //Lấy tất cả sản phẩm
    List<Product> getAllProduct();

}
