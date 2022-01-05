package com.springboot.springbootDemo;

import com.springboot.springbootDemo.model.Product;
import com.springboot.springbootDemo.repository.ProductRepository;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ProductRepository productRepository;

    HttpHeaders httpHeaders;

    @Before
    public void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    @After
    public void clear() {
        productRepository.deleteAll();
    }
    @Test
    public void testCreateProduct() throws Exception{

        JSONObject request = new JSONObject()
                .put("name", "Hi Hi")
                .put("price", 450);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/products")
                .headers(httpHeaders)
                .content(request.toString());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").hasJsonPath())
                .andExpect(jsonPath("$.name").value(request.getString("name")))
                .andExpect(jsonPath("$.price").value(request.getInt("price")))
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    private Product createProduct(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return product;
    }
    @Test
    public void testGetProduct() throws Exception {

        Product product = createProduct("Economics", 450);
        productRepository.insert(product);

        mockMvc.perform(get("/products/" + product.getId())
                        .headers(httpHeaders))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    public void testReplaceProduct() throws Exception {
        Product product = createProduct("Economics", 450);
        productRepository.insert(product);

        JSONObject request = new JSONObject()
                .put("name", "Macroeconomics")
                .put("price", 5500);

        mockMvc.perform(put("/products/" + product.getId())
                        .headers(httpHeaders)
                        .content(request.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(request.getString("name")))
                .andExpect(jsonPath("$.price").value(request.getInt("price")));
    }
    @Test(expected = RuntimeException.class)
    public void testDeleteProduct() throws Exception {
        Product product = createProduct("Economics123", 450);
        productRepository.insert(product);

        mockMvc.perform(delete("/products/" + product.getId())
                        .headers(httpHeaders))
                .andDo(print())
                .andExpect(status().isNoContent());

        productRepository.findById(product.getId())
                .orElseThrow(RuntimeException::new);
    }
}
