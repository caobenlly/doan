package com.example.doantotnghiep.model.responeadmin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdersAdminResponse {

    private Long id;
    private String nguoiNhan;
    private String dienThoai;
    private int trangThai;
    private String sanPham;
    private String ngayTao;
    private String ngaySua;


}
