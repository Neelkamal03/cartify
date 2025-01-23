package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    //An annotation used for excluding a field from being recursively called.
    //Prevents infinite recursion in bidirectional relationships.
    //Hides sensitive or unnecessary data in API responses.
    //also used for string password
    //Serialization:-A process of converting json object into a format that can be easily transferred like json or XML.
    //Deserialization:-The reverse process—converting JSON or XML back into a Java object.
    private List<Product> products;
}
