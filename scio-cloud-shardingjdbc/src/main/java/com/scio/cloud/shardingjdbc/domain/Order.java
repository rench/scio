package com.scio.cloud.shardingjdbc.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order")
public class Order {
  /** 主键id */
  @Id
  @Column(name = "id")
  // @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  /** 主键Id */
  @Column(name = "order_id", unique = true)
  private Long orderId;
  /** 订单标题 */
  @Column(name = "title")
  private String title;
  /** 订单金额 */
  @Column(name = "price")
  private BigDecimal price;
  /** 用户id */
  @Column(name = "user_id")
  private String userId;
  /** 创建时间 */
  @Column(name = "created_date")
  private Date createdDate;
  /** 修改时间 */
  @Column(name = "modified_date")
  private Date modifiedDate;
  /** @return the id */
  public Long getId() {
    return id;
  }
  /** @param id the id to set */
  public void setId(Long id) {
    this.id = id;
  }

  /** @return the orderId */
  public Long getOrderId() {
    return orderId;
  }
  /** @param orderId the orderId to set */
  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }
  /** @return the title */
  public String getTitle() {
    return title;
  }
  /** @param title the title to set */
  public void setTitle(String title) {
    this.title = title;
  }
  /** @return the price */
  public BigDecimal getPrice() {
    return price;
  }
  /** @param price the price to set */
  public void setPrice(BigDecimal price) {
    this.price = price;
  }
  /** @return the userId */
  public String getUserId() {
    return userId;
  }
  /** @param userId the userId to set */
  public void setUserId(String userId) {
    this.userId = userId;
  }
  /** @return the createdDate */
  public Date getCreatedDate() {
    return createdDate;
  }
  /** @param createdDate the createdDate to set */
  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }
  /** @return the modifiedDate */
  public Date getModifiedDate() {
    return modifiedDate;
  }
  /** @param modifiedDate the modifiedDate to set */
  public void setModifiedDate(Date modifiedDate) {
    this.modifiedDate = modifiedDate;
  }
}
