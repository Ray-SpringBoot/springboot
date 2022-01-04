package com.springboot.springbootDemo.service;

import com.springboot.springbootDemo.exception.NotFoundException;
import com.springboot.springbootDemo.exception.UnprocessableEntityException;
import com.springboot.springbootDemo.model.Product;
import com.springboot.springbootDemo.repository.MockProductDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private MockProductDAO productDAO;

    public Product createProduct(Product request){
        boolean isIdDuplicated = productDAO.find(request.getId()).isPresent();
        if(isIdDuplicated){
            throw new UnprocessableEntityException("THE ID OF THE PRODUCT IS DUPLICATED ");
        }
        Product product = new Product();
        product.setId(request.getId());
        product.setName(request.getName());
        product.setPrice(request.getPrice());

        return productDAO.insert(product);
    }

    public Product getProduct(String id){
        return productDAO.find(id)
                .orElseThrow(() -> new NotFoundException("Can't find product"));
    }

    public Product updateProject(String id, Product request){
        Product product = getProduct(id);
        return productDAO.replace(product.getId(), request);

    }

    public void deleteProduct(String id) {
        Product product = getProduct(id);
        productDAO.delete(product.getId());
    }

}
