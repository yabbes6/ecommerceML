package com.example.ecommerce.controllerImpl;


import com.example.ecommerce.consants.BillConstants;
import com.example.ecommerce.controller.BillController;
import com.example.ecommerce.model.Bill;
import com.example.ecommerce.service.BillService;
import com.example.ecommerce.utils.BillUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class BillControllerImpl implements BillController {
    @Autowired
    BillService billService;
    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        try{
            return billService.generateReport(requestMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        return BillUtils.getResponseEntity(BillConstants.CAFE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try{
            return billService.getBills();
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        try{
            return billService.getPdf(requestMap);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {

        try{
            return billService.deleteBill(id);
        }catch(Exception e){
            e.printStackTrace();
        }
        return BillUtils.getResponseEntity(BillConstants.CAFE_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
