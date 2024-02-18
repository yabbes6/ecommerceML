package com.example.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import java.io.Serializable;



@NamedQuery(name= "Product.getAllProduct", query="select new com.example.ecommerce.wrapper.ProductMapper(p.id,p.name,p.description,p.price,p.status,p.category.id,p.category.name) from Product p")
@NamedQuery(name="Product.updateProductStatus",query = "update Product p set p.status=:status where p.id=:id")
@NamedQuery(name = "Product.getProductByCategory" ,query = "select new com.example.ecommerce.wrapper.ProductMapper(p.id,p.name) from Product p where p.category.id =:id and p.status='true'")
@NamedQuery(name="Product.getProductById" , query="select new com.example.ecommerce.wrapper.ProductMapper(p.id,p.name,p.description,p.price) from Product p where p.id=:id" )

@Data@AllArgsConstructor@NoArgsConstructor
@Entity
public class Product implements Serializable {
    public static final Long serialVersionUid=123456L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_fk",nullable = false)
    private Category category;
    private String description;
    private double price;
    private String status;


}
