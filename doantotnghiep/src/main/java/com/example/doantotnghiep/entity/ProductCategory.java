package com.example.doantotnghiep.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "product_category")
public class ProductCategory {

    @Id
    @Column(name = "product_id")
    private String id;
    @Column(name = "category_id",nullable = false)
    private Long categoryId;
}
