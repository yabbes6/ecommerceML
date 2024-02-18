package com.example.ecommerce.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductMapper {

    Integer id;
    String name ;
    String description;
    double price;
    String status;
    Integer categoryId;
    String categoryName;

public ProductMapper(Integer id, String name ){
    this.id = id;
    this.name = name;
}
public ProductMapper(Integer id, String name , String description, double price){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price=price;
}
}
