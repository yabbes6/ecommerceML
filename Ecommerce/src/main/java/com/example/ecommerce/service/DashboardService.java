package com.example.ecommerce.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface DashboardService {
    public ResponseEntity<Map<String, Object>> getCount();
}
