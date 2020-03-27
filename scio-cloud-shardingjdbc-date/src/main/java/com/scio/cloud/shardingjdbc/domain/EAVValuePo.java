package com.scio.cloud.shardingjdbc.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "eav_value")
public class EAVValuePo {

    @Id
    private Long id;

    private Long entityId;

    private Long fieldId;

    private String fieldCode;

    private String fieldSource;

    private String value;

    private Date createTime;

    private Date updateTime;
}