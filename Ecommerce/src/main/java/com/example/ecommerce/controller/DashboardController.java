package com.example.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")

@RequestMapping(path = "/dashboard")
public interface DashboardController {

    @GetMapping(path = "/details")
    ResponseEntity<Map<String,Object>> getCount();
}
