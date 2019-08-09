package com.lv.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "es_job")
public class Job {

    @Id
    private String jobId;

    private String jobName;

    private BigDecimal salary;

}
