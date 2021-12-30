package com.springboot.springbootDemo.model.param;

import lombok.Data;

@Data
public class ProductQueryParameter {
    private String keyword;
    private String orderBy;
    private String sortRule;
}
