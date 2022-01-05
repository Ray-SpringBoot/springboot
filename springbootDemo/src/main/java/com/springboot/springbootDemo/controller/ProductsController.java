package com.springboot.springbootDemo.controller;

import com.springboot.springbootDemo.model.Product;
import com.springboot.springbootDemo.model.param.ProductQueryParameter;
import com.springboot.springbootDemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/products",produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductsController {

    @Autowired
    private ProductService service;

    //GET
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id) throws Throwable {
       Product product = service.getProduct(id);
        return ResponseEntity.ok().body(product);
    }

    //POST
    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody Product request){

       Product product = service.createProduct(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(product.getId())
                .toUri();

        return ResponseEntity.created(location).body(product);
    }

    //PUT
    @PutMapping("/{id}")
    public ResponseEntity<Product> replaceProduct(
            @PathVariable("id") String id,@Valid @RequestBody Product request) {
        Product product = service.replaceProduct(id, request);
        return ResponseEntity.ok(product);
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
//
//    //Get By Keyword
//    @GetMapping
//    public ResponseEntity<List<Product>> getProducts(@RequestParam(value = "keyword",defaultValue = "") String name){
//        List<Product> products = productDB.stream()
//                .filter(product -> product.getName().toUpperCase().contains(name.toUpperCase(Locale.ROOT)))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok().body(products);
//    }
//
//    //Get By Keyword with  @ModelAttribute
//    @GetMapping("/sort")
//    public ResponseEntity<List<Product>> getProducts(@ModelAttribute ProductQueryParameter parameter){
//        String keyword = parameter.getKeyword();
//        String orderBy = parameter.getOrderBy();
//        String sortRule = parameter.getSortRule();
//        Comparator<Product> comparator = genSortComparator(orderBy, sortRule);
//
//        List<Product> products = productDB.stream()
//                .filter(product -> product.getName().toUpperCase().contains(keyword.toUpperCase()))
//                .sorted(comparator)
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok().body(products);
//    }
//
//    private Comparator<Product> genSortComparator(String orderBy, String sortRule) {
//        Comparator<Product> comparator = (p1, p2) -> 0;
//        if (Objects.isNull(orderBy) || Objects.isNull(sortRule)) {
//            return comparator;
//        }
//
//        if (orderBy.equalsIgnoreCase("price")) {
//            comparator = Comparator.comparing(Product::getPrice);
//        } else if (orderBy.equalsIgnoreCase("name")) {
//            comparator = Comparator.comparing(Product::getName);
//        }
//
//        return sortRule.equalsIgnoreCase("desc")
//                ? comparator.reversed()
//                : comparator;
//    }
}
