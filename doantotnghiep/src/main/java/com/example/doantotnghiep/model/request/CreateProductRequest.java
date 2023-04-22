package com.example.doantotnghiep.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateProductRequest {
    private String id;

    @NotBlank(message = "Tên sản phẩm trống!")
    @Size(max = 300, message = "Tên sản phẩm có độ dài tối đa 300 ký tự!")
    private String name;

    @NotBlank(message = "Mô tả sản phẩm trống!")
    private String description;

    @NotNull(message = "Nhãn hiệu trống!")
    private Long brandId;

    @NotNull(message = "Danh mục trống!")
    private ArrayList<Integer> categoryIds;

    @Min(1000)
    @Max(1000000000)
    @NotNull(message = "Giá sản phẩm trống!")
    private Long price;

    @Min(1000)
    @Max(1000000000)
    @NotNull(message = "Giá bán sản phẩm trống!")
    private Long salePrice;

    @NotNull(message = "Danh sách ảnh trống!")
    private ArrayList<String> images;


    private ArrayList<String> feedBackImages;

    private int status;
}
