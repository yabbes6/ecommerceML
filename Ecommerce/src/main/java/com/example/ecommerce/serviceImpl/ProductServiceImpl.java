package com.example.ecommerce.serviceImpl;

import com.example.ecommerce.JWT.JwtFilter;
import com.example.ecommerce.consants.ProduitConstants;
import com.example.ecommerce.dao.ProductRepository;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.utils.CafeUtils;
import com.example.ecommerce.wrapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        Product product;
        try{
            if (jwtFilter.isAdmin()){
                if (validateProduitMap(requestMap,false)){
                    productRepository.save(getProduitFromMap(requestMap,false));
                    return CafeUtils.getResponseEntity("Product Added Successfully",HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity(ProduitConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
            }else
                return CafeUtils.getResponseEntity(ProduitConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
        }catch(Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(ProduitConstants.UNAUTHORIZED_ACCESS, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductMapper>> getAllProduct() {
        try{
            return new ResponseEntity<>(productRepository.getAllProduct(),HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private Product getProduitFromMap(Map<String,String> requestMap, boolean isAdd){
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));
        Product product = new Product();
        if (isAdd)
            product.setId(Integer.parseInt(requestMap.get("id")));
        else
            product.setStatus("true");

        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Double.parseDouble(requestMap.get("price")));

        return product;
    }
    private boolean validateProduitMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")){
            if (requestMap.containsKey("id") && validateId){
                return true ;
            }else if(!validateId){
                return true;
            }
        }
        return false;
    }
    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try{
            if (jwtFilter.isAdmin()){
                if (validateProduitMap(requestMap,true)){
                    Optional<Product> optional = productRepository.findById(Integer.parseInt(requestMap.get("id")));
                    if (!optional.isEmpty()){
                        Product product = getProduitFromMap(requestMap,true);
                        product.setStatus(optional.get().getStatus());
                        productRepository.save(product);
                        return CafeUtils.getResponseEntity("Product updated successfuly",HttpStatus.OK);
                    }else{
                        return CafeUtils.getResponseEntity("Product id doest not exist .",HttpStatus.OK);
                    }
                }else {
                    return CafeUtils.getResponseEntity(ProduitConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);
                }
            }
            else {
                return CafeUtils.getResponseEntity(ProduitConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(ProduitConstants.INVALID_DATA,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try{
            Optional optional = productRepository.findById(id);
            if (!optional.isEmpty()){
                productRepository.deleteById(id);
                return CafeUtils.getResponseEntity("Product  deleted successfuly",HttpStatus.OK);
            }else{
                return CafeUtils.getResponseEntity("Product id does not exist" ,HttpStatus.OK);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(ProduitConstants.CAFE_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try{
            Optional optional = productRepository.findById(Integer.parseInt(requestMap.get("id")));
            if (!optional.isEmpty()){
                productRepository.updateProductStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
                return CafeUtils.getResponseEntity("Product status updated successfuly",HttpStatus.OK);
            }else {
                return CafeUtils.getResponseEntity("Product status does not updated",HttpStatus.OK);
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        return CafeUtils.getResponseEntity(ProduitConstants.CAFE_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductMapper>> getByCategory(Integer id) {
        try{
            return new ResponseEntity<>(productRepository.getProductByCategory(id),HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductMapper> getProductById(Integer id) {
        try {
            return new ResponseEntity<>(productRepository.getProductById(id),HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ProductMapper(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
