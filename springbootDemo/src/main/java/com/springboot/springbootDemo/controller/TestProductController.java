package com.springboot.springbootDemo.controller;

import com.springboot.springbootDemo.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
public class TestProductController {

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable("id") String id ){
        Product product = new Product();
        product.setId("A01");
        product.setName("Test");
        product.setPrice(0);
        return product;
    }

}
