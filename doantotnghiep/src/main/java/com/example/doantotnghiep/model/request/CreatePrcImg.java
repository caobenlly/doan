package com.example.doantotnghiep.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreatePrcImg {

        private String id;

        @NotBlank(message = "Tên sản phẩm trống!")
        @Size(max = 300, message = "Tên sản phẩm có độ dài tối đa 300 ký tự!")
        private String name;

        @NotBlank(message = "Mô tả sản phẩm trống!")
        @NonNull
        private String description;

        @NotNull(message = "Nhãn hiệu trống!")
        @JsonProperty("brand_id")
        private Long brandId;

        @NotNull(message = "Danh mục trống!")
        @JsonProperty("category_ids")
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
        @JsonProperty("product_images")
        private ArrayList<MultipartFile> images;

        @JsonProperty("feed_back_images")
        private ArrayList<String> feedBackImages;
        private int status;


}
