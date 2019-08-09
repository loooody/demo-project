package com.lv.demo.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Data
@Document(indexName = "mysql01", type = "jdbc")
public class EsUser {

    private Integer id;

    private String name;

    private Integer age;

    private Date create_time;

}

