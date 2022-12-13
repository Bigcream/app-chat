package com.example.appchat.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "category")
public class CategoryEntity {

    @Id
    @SequenceGenerator(name = "category_id_seq", sequenceName = "category_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_id_seq")
    protected Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @OneToMany(mappedBy = "categoryP")
    private List<ProductEntity> products = new ArrayList<>();
}
