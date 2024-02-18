package com.example.ecommerce.model;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.io.Serializable;


@NamedQuery(name = "Category.getAllCategory",query = "select c from Category c where c.id in (select p.category from Product p where p.status='true')")
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //private Produit produit;
    @Nullable
    private String name;
}
