/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.doantotnghiep.controller.shop;


import com.example.doantotnghiep.model.responeadmin.ThanhToanRespone;
import com.google.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author CTT VNPAY
 */

@RestController
@CrossOrigin("*")
public class vnpayRefund extends HttpServlet {

    @GetMapping ("/thongtinthanhtoantest")
    protected ResponseEntity<?> doCheck(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        Map fields = new HashMap();
        for (Enumeration params = request.getParameterNames(); params.hasMoreElements(); ) {
            String fieldName = (String) params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        if (fields.containsKey("vnp_SecureHashType")) {
            fields.remove("vnp_SecureHashType");
        }
        if (fields.containsKey("vnp_SecureHash")) {
            fields.remove("vnp_SecureHash");
        }
        String signValue = Config.hashAllFields(fields);

//        if (signValue.equals(vnp_SecureHash)) {
            if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                ThanhToanRespone rp = new ThanhToanRespone();
                rp.setMagiaodich(request.getParameter("vnp_BankTranNo"));
                rp.setSotien(request.getParameter("vnp_Amount"));
                rp.setStatus("GD Thành Công");
                rp.setTendonhang(request.getParameter("vnp_OrderInfo"));
                return ResponseEntity.ok(rp);
            } else {
                return ResponseEntity.ok("GD Không Thành Công");
            }

//        else {
//            resp.getWriter().write("Chu ky khong hop le");
//        }
    }
}
