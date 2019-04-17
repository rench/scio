package com.scio.cloud.querydsl.web.vo;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.util.Date;

import com.scio.cloud.querydsl.domain.enums.UserStatus;
/**
 * user info vo
 *
 * @author Wang.ch
 * @date 2019-04-15 10:27:58
 */
public class UserInfoVo {

  private Long id;

  private String username;

  private String realname;

  private String password;

  private Date registerTime;

  private String registerIp;

  private UserStatus status;

  /** @return the id */
  public Long getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(Long id) {
    this.id = id;
  }

  /** @return the username */
  public String getUsername() {
    return username;
  }

  /** @param username the username to set */
  public void setUsername(String username) {
    this.username = username;
  }

  /** @return the realname */
  public String getRealname() {
    return realname;
  }

  /** @param realname the realname to set */
  public void setRealname(String realname) {
    this.realname = realname;
  }

  /** @return the password */
  public String getPassword() {
    return password;
  }

  /** @param password the password to set */
  public void setPassword(String password) {
    this.password = password;
  }

  /** @return the registerTime */
  public Date getRegisterTime() {
    return registerTime;
  }

  /** @param registerTime the registerTime to set */
  public void setRegisterTime(Date registerTime) {
    this.registerTime = registerTime;
  }

  /** @return the registerIp */
  public String getRegisterIp() {
    return registerIp;
  }

  /** @param registerIp the registerIp to set */
  public void setRegisterIp(String registerIp) {
    this.registerIp = registerIp;
  }

  /** @return the status */
  public UserStatus getStatus() {
    return status;
  }

  /** @param status the status to set */
  public void setStatus(UserStatus status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return reflectionToString(this);
  }
}
