package com.example.ecommerce.controllerImpl;

import com.example.ecommerce.consants.ProduitConstants;
import com.example.ecommerce.controller.ProductController;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.utils.CafeUtils;
import com.example.ecommerce.wrapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ProductControllerImpl implements ProductController {

    @Autowired
    ProductService productService;
    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try{
            return productService.addNewProduct(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(ProduitConstants.UNAUTHORIZED_ACCESS, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductMapper>> getAllProduct() {
        try{
            return productService.getAllProduct();
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try{
            return productService.updateProduct(requestMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(ProduitConstants.CAFE_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {

        try{
            return productService.deleteProduct(id);
        }catch(Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(ProduitConstants.CAFE_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try{
            return productService.updateStatus(requestMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(ProduitConstants.CAFE_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductMapper>> getByCategory(Integer id) {
        try{
           return  productService.getByCategory(id);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductMapper> getProductById(Integer id) {
        try{
            return productService.getProductById(id);
        }catch( Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ProductMapper(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
