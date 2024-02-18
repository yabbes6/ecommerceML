package com.example.ecommerce.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;


@NamedQuery(name= "Bill.getAllBills", query="select b from Bill b order by b.id desc")
@NamedQuery(name= "Bill.getBillByUsername", query="select b from Bill b where b.createdBy=:username order by b.id desc")

@Entity
@Data@AllArgsConstructor@NoArgsConstructor
public class Bill implements Serializable {

    private static final long serialVersionID = 1L;
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id ;
    private String uuid;
    private String name;
    private String email;
    private String contactNumber;
    private String paymentMethod;
    private double total;
    private String productDetail;
    private String createdBy;

}
