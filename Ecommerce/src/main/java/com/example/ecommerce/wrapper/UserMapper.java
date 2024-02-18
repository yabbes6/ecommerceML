package com.example.ecommerce.wrapper;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMapper {

    private Integer id;
    private String name;
    private String email;
    private String contactNumber;
    private String status;


}
