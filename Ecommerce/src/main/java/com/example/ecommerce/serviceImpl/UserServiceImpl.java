package com.example.ecommerce.serviceImpl;


import com.example.ecommerce.JWT.CustomerUsersDetailsService;
import com.example.ecommerce.JWT.JwtFilter;
import com.example.ecommerce.JWT.JwtUtil;
import com.example.ecommerce.consants.CafeConstants;
import com.example.ecommerce.dao.UserRepository;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.utils.CafeUtils;
import com.example.ecommerce.utils.EmailUtils;
import com.example.ecommerce.wrapper.UserMapper;
import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    EmailUtils emailUtils;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}",requestMap);
        try{
            if (validateSignUp(requestMap)){
                User user = userRepository.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)){
                    userRepository.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("Successfully registered ",HttpStatus.OK);
                }else {
                    return CafeUtils.getResponseEntity("Email already exists",HttpStatus.BAD_REQUEST);
                }
            }
            else {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.CAFE_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private boolean validateSignUp(Map<String ,String> requestMap){
        if (requestMap.containsKey("name") &&
            requestMap.containsKey("contactNumber") &&
            requestMap.containsKey("email") &&
            requestMap.containsKey("password") )
            return true;
        else
            return false;
    }
    private User getUserFromMap(Map<String,String> requestMap){
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");

        return user;
    }


    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))
            );
            if(auth.isAuthenticated()){
                if (customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\""
                            + jwtUtil.generateToken(
                                    customerUsersDetailsService.getUserDetail().getEmail(),
                                    customerUsersDetailsService.getUserDetail().getRole()) + "\"}",
                            HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<String>("{\"message\":\""+ "wait for admin approval,"+"\"}",HttpStatus.BAD_REQUEST);
                }
            }
        }catch(Exception e){
           log.error("{}",e);
        }
        return new ResponseEntity<String>("{\"message\":\""+ "Bad Credentials"+"\"}",HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<List<UserMapper>> getAllUser() {
        try{
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(userRepository.getAllUser(),HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try{
            if (jwtFilter.isAdmin()){
                Optional<User> optional=userRepository.findById(Integer.parseInt(requestMap.get("id")));
                if(!optional.isEmpty()){
                    userRepository.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"),optional.get().getEmail(),userRepository.getAllAdmin());
                    return CafeUtils.getResponseEntity("User status updated successfully",HttpStatus.OK);
                }else{
                    CafeUtils.getResponseEntity("User id does not exist",HttpStatus.OK);
                }
            }else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.CAFE_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {

        allAdmin.remove(jwtFilter.getCurrentUser());

        if(status!=null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account Approved","USER:- "+ user +"\n is approved by \nADMIN:- "+ jwtFilter.getCurrentUser(),allAdmin);
        }else{
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account Disabled","USER:- "+ user +"\n is disabled by \nADMIN:- "+ jwtFilter.getCurrentUser(),allAdmin);

        }
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return CafeUtils.getResponseEntity("true",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj = userRepository.findByEmail(jwtFilter.getCurrentUser());
            if (!userObj.equals(null)){
                if (userObj.getPassword().equals(requestMap.get("oldPassword"))){
                    userObj.setPassword(requestMap.get("newPassword"));
                    userRepository.save(userObj);
                    return CafeUtils.getResponseEntity("password updated Successfully",HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Incorrect old Password",HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.CAFE_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.CAFE_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try{
            User user= userRepository.findByEmail(requestMap.get("email"));
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
                emailUtils.forgotMail(user.getEmail(),"Credentials by Cafe management System",user.getPassword());
                return CafeUtils.getResponseEntity("Check your mail for Credentials",HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.CAFE_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}





















