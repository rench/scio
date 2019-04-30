package com.scio.cloud.shardingjdbc.web.vo;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
/**
 * user info vo
 *
 * @author Wang.ch
 * @date 2019-04-15 10:27:58
 */
public class UserInfoVo {
  /** id */
  private String id;
  /** name */
  private String name;
  /** mobile */
  private String mobile;
  /** address */
  private String address;
  /** locked */
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
