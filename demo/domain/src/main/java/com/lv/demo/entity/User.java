package com.lv.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name="es_user")
public class User {

    @Id
    private Integer id;

    private String name;

    private String age;

}
