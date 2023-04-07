package com.example.doantotnghiep.model.responeadmin;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrdersAdminResponse {

    String maDonhang;
    String nguoiNhan;
    String dienThoai;
    String trangThai;
    String sanPham;
    String ngayTao;
    String ngaySua;
}
