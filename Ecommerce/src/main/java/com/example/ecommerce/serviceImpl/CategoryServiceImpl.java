package com.example.ecommerce.serviceImpl;

import com.example.ecommerce.JWT.JwtFilter;
import com.example.ecommerce.consants.ProduitConstants;
import com.example.ecommerce.dao.CategoryRepository;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.utils.CafeUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try{
            if (jwtFilter.isAdmin()){
                if (validateCategoryMap(requestMap,false)){
                    categoryRepository.save(getCategoryFromMap(requestMap,false));
                    return CafeUtils.getResponseEntity("Category Added Successfully",HttpStatus.OK);
                }
            }else {
                return CafeUtils.getResponseEntity(ProduitConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(ProduitConstants.CAFE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")){
            if (requestMap.containsKey("id") && validateId) return true;
            else if (!validateId){
                return true ;
            }
        }
        return false;
    }
    private Category getCategoryFromMap(Map<String,String> requestMap, Boolean isAdd){
        Category category = new Category();
        if (isAdd){
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try{
            if (!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
                log.info("inside if");
                return new ResponseEntity<List<Category>>(categoryRepository.getAllCategory(), HttpStatus.OK);
            }
            return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>() ,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try{
            if (jwtFilter.isAdmin()){
                if (validateCategoryMap(requestMap,true)){
                   Optional optional= categoryRepository.findById(Integer.parseInt((requestMap.get("id"))));
                   if (!optional.isEmpty()){
                       categoryRepository.save(getCategoryFromMap(requestMap,true));
                       return CafeUtils.getResponseEntity("Updated Successfully",HttpStatus.OK);
                   }else {
                       return CafeUtils.getResponseEntity("Category id does not exist",HttpStatus.OK);
                   }
                }
            }else {
                return CafeUtils.getResponseEntity(ProduitConstants.UNAUTHORIZED_ACCESS,HttpStatus.BAD_REQUEST);
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(ProduitConstants.CAFE_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
