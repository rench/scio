package com.scio.cloud.beetlsql.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Wang.ch
 * @since 2019-04-25
 */
public class UserInfo implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private String password;

  private String realname;

  private String registerIp;

  private LocalDateTime registerTime;

  private Integer status;

  private String username;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRealname() {
    return realname;
  }

  public void setRealname(String realname) {
    this.realname = realname;
  }

  public String getRegisterIp() {
    return registerIp;
  }

  public void setRegisterIp(String registerIp) {
    this.registerIp = registerIp;
  }

  public LocalDateTime getRegisterTime() {
    return registerTime;
  }

  public void setRegisterTime(LocalDateTime registerTime) {
    this.registerTime = registerTime;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String toString() {
    return "UserInfo{"
        + "id="
        + id
        + ", password="
        + password
        + ", realname="
        + realname
        + ", registerIp="
        + registerIp
        + ", registerTime="
        + registerTime
        + ", status="
        + status
        + ", username="
        + username
        + "}";
  }
}
