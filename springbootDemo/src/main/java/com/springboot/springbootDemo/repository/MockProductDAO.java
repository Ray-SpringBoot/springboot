package com.springboot.springbootDemo.repository;

import com.springboot.springbootDemo.model.Product;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MockProductDAO {

    // (以List模擬為資料庫)
    private final List<Product> productDB = new ArrayList<>();

    // Controller 被建立後，自動執行該方法，新增預設的產品資料
    @PostConstruct
    private void initDB(){
        productDB.add(new Product("A01","Backend",250));
        productDB.add(new Product("A02","Web",150));
        productDB.add(new Product("A03","APP",350));
        productDB.add(new Product("A04","QA",300));
    }

    public Product insert(Product product){
        productDB.add(product);
        return product;
    }

    public Optional<Product> find(String id){
        return productDB.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    public Product replace(String id, Product product){
        Optional<Product> productOptional = find(id);
        productOptional.ifPresent(product1 -> {
            product.setName(product.getName());
            product.setPrice(product.getPrice());
        });
        return product;
    }

    public void delete(String id){
        productDB.removeIf(product -> product.getId().equals(id));
    }
}
