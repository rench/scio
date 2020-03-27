package com.scio.cloud.shardingjdbc.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "eav_entity")
@Entity
public class EAVEntityPo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loanAppCode;

    private String entityCode;

    private String categoryCode;

    private String categoryType;

    private Long parentId;

    private Integer deleted = 0;

    @Column(insertable = false, updatable = false)
    private Date createTime;
    @Column(insertable = false, updatable = false)
    private Date updateTime;
    @Column(insertable = false, updatable = false)
    private Date biTimestamp;
}