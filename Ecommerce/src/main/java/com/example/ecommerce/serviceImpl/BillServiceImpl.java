package com.example.ecommerce.serviceImpl;


import com.example.ecommerce.consants.BillConstants;
import com.example.ecommerce.dao.BillRepository;
import com.example.ecommerce.model.Bill;
import com.example.ecommerce.service.BillService;
import com.example.ecommerce.utils.BillUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    BillRepository billRepository;
    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        try{
            String fileName;
            if(validateRequestMap(requestMap)){
                if (requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate")){
                    fileName = (String) requestMap.get("uuid");
                }else {
                    fileName = BillUtils.getUuid();
                    requestMap.put("uuid",fileName);
                    insertBill(requestMap);
                }

                String data = "Name : " + requestMap.get("name") + "\n"+"Contact Number: " + requestMap.get("contactNumber")+
                        "\n"+"Email: "+requestMap.get("email") + "\n" + "Payment Method: " + requestMap.get("paymentMethod");

                Document document= new Document();
                PdfWriter.getInstance(document,new FileOutputStream(BillConstants.STORE_LOCATION+"\\"+fileName+".pdf"));
                document.open();
                setRectangleInPdf(document);

                Paragraph paragraph = new Paragraph("Cafe Management System",getFont("Header"));
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);

                Paragraph paragraph1 = new Paragraph(data+"\n \n",getFont("Data"));
                document.add(paragraph1);

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHeader(table);

                JSONArray jsonArray = BillUtils.getJsonArrayFromString((String)requestMap.get("productDetail"));

                for (int i= 0 ;i<jsonArray.length();i++){
                    addRows(table,BillUtils.getMapFromJson(jsonArray.getString(i)));
                }
                document.add(table);
                Paragraph footer = new Paragraph(("Total : "+ requestMap.get("total")+"\n"
                +"Thank you"),getFont("Data"));
                document.add(footer);
                document.close();
                return new ResponseEntity<>("{\"uui\":\""+fileName+"\"}",HttpStatus.OK);
            }
            return BillUtils.getResponseEntity("Required data not found",HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }

        return BillUtils.getResponseEntity(BillConstants.CAFE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    private void addRows(PdfPTable table, Map<String, Object> data) {
        log.info("inside row");
        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString((Double)data.get("price")));
        table.addCell(Double.toString((Double)data.get("total")));

    }

    private void addTableHeader(PdfPTable table) {
        log.info("inside table");

        Stream.of("Name","Category","Quantity","Price","Sub Total").forEach(title ->{
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            header.setBorderWidth(2);
            header.setPhrase(new Phrase(title));
            header.setBackgroundColor(BaseColor.GREEN);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setVerticalAlignment(Element.ALIGN_CENTER);
            table.addCell(header);
        });
    }

    private Font getFont(String type){
        log.info("inside font");
        switch(type){
            case "Header":Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE,18,BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,11,BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:return new Font();
        }
    }
    private void setRectangleInPdf(Document document)throws DocumentException {
        log.info("Inside set" +
                "" +
                "Rectangel");
        Rectangle rectangle = new Rectangle(577,825,18,15);
        rectangle.enableBorderSide(1);
        rectangle.enableBorderSide(2);
        rectangle.enableBorderSide(4);
        rectangle.enableBorderSide(8);
        rectangle.setBorderColor(BaseColor.BLACK);
        rectangle.setBorderWidth(1);
        document.add(rectangle);


    }

    private void insertBill(Map<String, Object> requestMap) {
        try{
            Bill bill = new Bill();
            bill.setUuid((String) requestMap.get("uuid"));
            bill.setName((String)requestMap.get("name"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setContactNumber((String) requestMap.get("contactNumber"));
            bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
            bill.setTotal(Double.parseDouble((String) requestMap.get("total")));
            bill.setProductDetail((String) requestMap.get("productDetail"));
            bill.setCreatedBy("user");
            billRepository.save(bill);

        }catch(Exception e ){
            e.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("name") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("paymentMethod") &&
                requestMap.containsKey("productDetails") &&
                requestMap.containsKey("total");

    }


    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try{
            List<Bill> list = new ArrayList<>();
            list = billRepository.getAllBills();
            if(!list.isEmpty())
                return new ResponseEntity<>(list,HttpStatus.OK);
            else
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.OK);

        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        log.info("inside get PDF : {}",requestMap);
        try{
            byte[] byteArray = new byte[0];
            if(requestMap.containsKey("uuid") && validateRequestMap(requestMap)){
                return new ResponseEntity<>(byteArray,HttpStatus.BAD_REQUEST);
            }
            String filePath = BillConstants.STORE_LOCATION+"\\"+(String) requestMap.get("uuid")+".pdf";
            if (BillUtils.isFileExist(filePath)){
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray,HttpStatus.OK);
            }
            else {
                requestMap.put("isGenerate", false);
                generateReport(requestMap);
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try{
            Optional optional= billRepository.findById(id);
            if (!optional.isEmpty()){
                billRepository.deleteById(id);
                return BillUtils.getResponseEntity("Bill deleted successfuly",HttpStatus.OK);
            }
            return BillUtils.getResponseEntity("Bill does not exist" , HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
        }
        return BillUtils.getResponseEntity(BillConstants.CAFE_ERROR,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private byte[] getByteArray(String filePath) throws Exception {
        File initialFile = new File(filePath);
        InputStream inputStream = new FileInputStream(initialFile);
        byte[] byteArray = IOUtils.toByteArray(inputStream);
        return byteArray;

    }


}
