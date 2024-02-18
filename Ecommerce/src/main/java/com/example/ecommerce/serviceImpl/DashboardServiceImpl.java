package com.example.ecommerce.serviceImpl;

import com.example.ecommerce.dao.BillRepository;
import com.example.ecommerce.dao.CategoryRepository;
import com.example.ecommerce.dao.ProductRepository;
import com.example.ecommerce.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    BillRepository billRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;
    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String ,Object> map = new HashMap<>();
        map.put("category",categoryRepository.count());//categoryRepository.count()
        map.put("product",productRepository.count());//productRepository.count()
        map.put("bill",billRepository.count());//billRepository.count()

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
