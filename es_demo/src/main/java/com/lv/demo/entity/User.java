package com.lv.demo.entity;

import lombok.Data;
import org.hibernate.annotations.Proxy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Proxy(lazy = false)
@Table(name = "es_user")
public class User {

    @Id
    private Integer id;

    private String name;

    private Integer age;

    private Date createTime;

}
