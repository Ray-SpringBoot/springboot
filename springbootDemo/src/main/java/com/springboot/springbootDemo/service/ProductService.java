package com.springboot.springbootDemo.service;

import com.springboot.springbootDemo.exception.NotFoundException;
import com.springboot.springbootDemo.model.Product;
import com.springboot.springbootDemo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public List<Product> getAll(){
       return repository.findAll();
    }

    public Product getProduct(String id) {
        return repository.findById(id)
                .orElseThrow(()->new NotFoundException("Can't find product."));

    }

    public Product createProduct(Product request){
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return repository.insert(product);
    }

    public Product replaceProduct(String id, Product request){
        Product product = getProduct(id);
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return repository.save(product);
    }

    public void deleteProduct(String id){
        repository.deleteById(id);
    }
}
