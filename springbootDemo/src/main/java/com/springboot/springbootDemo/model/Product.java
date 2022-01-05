package com.springboot.springbootDemo.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Data
@Document("products")
public class Product {
    private String id;

    @NotEmpty(message = "Product name is undefined")
    private String name;

    @Min(0)
    private int price;

    public Product() {
    }

    public Product(String id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

}
