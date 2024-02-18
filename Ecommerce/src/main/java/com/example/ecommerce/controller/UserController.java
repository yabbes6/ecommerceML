package com.example.ecommerce.controller;

import com.example.ecommerce.wrapper.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "/user")
public interface UserController {

    @PostMapping(path = "/signup")
    public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String,String> requestMap);

    @PostMapping(path = "/login")
    public ResponseEntity<String> login (@RequestBody(required = true) Map<String , String> requestMap);

    @GetMapping(path = "/get")
    public ResponseEntity<List<UserMapper>> getAllUser();

    @PostMapping(path = "/update")
    public ResponseEntity<String> update(@RequestBody(required = true) Map<String,String> requestMap);


    @GetMapping(path = "/checkToken")
    ResponseEntity<String> checkToken();

    @PostMapping(path = "/changePassword")
    ResponseEntity<String> changerPassword(@RequestBody Map<String,String> requestMap);


    @PostMapping(path = "/forgetPassword")
    ResponseEntity<String> forgetPassword(@RequestBody Map<String,String> requestMap);
}
