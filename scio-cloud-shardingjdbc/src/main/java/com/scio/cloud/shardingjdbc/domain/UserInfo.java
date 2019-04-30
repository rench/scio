package com.scio.cloud.shardingjdbc.domain;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
/**
 * user info entity
 *
 * @author Wang.ch
 * @date 2019-04-15 09:36:54
 */
@Entity
@Table(
    name = "user_info",
    uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class UserInfo implements Serializable {
  /** */
  private static final long serialVersionUID = 3476779772816848178L;

  @Id
  @Column(name = "id")
  private String id;
  /** name */
  @Column(name = "name", length = 20)
  private String name;
  /** mobile */
  @Column(name = "mobile")
  private String mobile;
  /** address */
  @Column(name = "address")
  private String address;
  /** locked */
  @Column(name = "locked")
  private boolean locked;

  /** @return the id */
  public String getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(String id) {
    this.id = id;
  }

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }

  /** @return the mobile */
  public String getMobile() {
    return mobile;
  }

  /** @param mobile the mobile to set */
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  /** @return the address */
  public String getAddress() {
    return address;
  }

  /** @param address the address to set */
  public void setAddress(String address) {
    this.address = address;
  }

  /** @return the locked */
  public boolean isLocked() {
    return locked;
  }

  /** @param locked the locked to set */
  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  @Override
  public String toString() {
    return reflectionToString(this);
  }
}
