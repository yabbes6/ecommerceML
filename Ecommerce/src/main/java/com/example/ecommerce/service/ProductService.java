package com.example.ecommerce.service;

import com.example.ecommerce.wrapper.ProductMapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ProductService {

    public ResponseEntity<String> addNewProduct(Map<String,String> requestMap);

    public ResponseEntity<List<ProductMapper>> getAllProduct();

    public ResponseEntity<String> updateProduct(Map<String, String> requestMap);

    public ResponseEntity<String> deleteProduct(Integer id);

    public ResponseEntity<String> updateStatus(Map<String, String> requestMap);

    public ResponseEntity<List<ProductMapper>> getByCategory(Integer id);

    public ResponseEntity<ProductMapper> getProductById(Integer id);
}
