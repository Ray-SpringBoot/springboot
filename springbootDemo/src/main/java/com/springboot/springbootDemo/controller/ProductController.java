package com.springboot.springbootDemo.controller;

import com.springboot.springbootDemo.model.Product;
import com.springboot.springbootDemo.model.param.ProductQueryParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/product",produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {

    // 暫存資料(以List假設為資料庫)
    private final List<Product> productDB = new ArrayList<>();

    // Controller 被建立後，自動執行該方法，新增預設的產品資料
    @PostConstruct
    private void initDB(){
        productDB.add(new Product("A01","Backend",250));
        productDB.add(new Product("A02","Web",150));
        productDB.add(new Product("A03","APP",350));
        productDB.add(new Product("A04","QA",300));
    }
    //GET
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") String id){
        Optional<Product> productOp = productDB.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        if(productOp.isPresent()){
            return ResponseEntity.ok().body(productOp.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    //POST
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product request){
        boolean isIdDuplicated = productDB.stream()
                .anyMatch(p -> p.getId().equals(request.getId()));
        if (isIdDuplicated) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        Product product = new Product();
        product.setId(request.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        productDB.add(product);

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
            @PathVariable("id") String id, @RequestBody Product request) {
        Optional<Product> productOp = productDB.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        if (productOp.isPresent()) {
            Product product = productOp.get();
            product.setName(request.getName());
            product.setPrice(request.getPrice());

            return ResponseEntity.ok().body(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") String id) {
        boolean isRemoved = productDB.removeIf(p -> p.getId().equals(id));

        return isRemoved
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    //Get By Keyword
    @GetMapping
    public ResponseEntity<List<Product>> getProducts(@RequestParam(value = "keyword",defaultValue = "") String name){
        List<Product> products = productDB.stream()
                .filter(product -> product.getName().toUpperCase().contains(name.toUpperCase(Locale.ROOT)))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(products);
    }

    //Get By Keyword with  @ModelAttribute
    @GetMapping("/sort")
    public ResponseEntity<List<Product>> getProducts(@ModelAttribute ProductQueryParameter parameter){
        String keyword = parameter.getKeyword();
        String orderBy = parameter.getOrderBy();
        String sortRule = parameter.getSortRule();
        Comparator<Product> comparator = genSortComparator(orderBy, sortRule);

        List<Product> products = productDB.stream()
                .filter(product -> product.getName().toUpperCase().contains(keyword.toUpperCase()))
                .sorted(comparator)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(products);
    }

    private Comparator<Product> genSortComparator(String orderBy, String sortRule) {
        Comparator<Product> comparator = (p1, p2) -> 0;
        if (Objects.isNull(orderBy) || Objects.isNull(sortRule)) {
            return comparator;
        }

        if (orderBy.equalsIgnoreCase("price")) {
            comparator = Comparator.comparing(Product::getPrice);
        } else if (orderBy.equalsIgnoreCase("name")) {
            comparator = Comparator.comparing(Product::getName);
        }

        return sortRule.equalsIgnoreCase("desc")
                ? comparator.reversed()
                : comparator;
    }
}
