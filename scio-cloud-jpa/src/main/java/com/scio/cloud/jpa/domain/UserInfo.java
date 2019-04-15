package com.scio.cloud.jpa.domain;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.scio.cloud.jpa.domain.enums.UserStatus;
/**
 * user info entity
 *
 * @author Wang.ch
 * @date 2019-04-15 09:36:54
 */
@Entity
@Table(name = "user_info")
public class UserInfo implements Serializable {
  /** */
  private static final long serialVersionUID = 3476779772816848178L;

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @Column(name = "username", length = 20)
  private String username;

  @Column(name = "realname", length = 50)
  private String realname;

  @Column(name = "password", length = 128)
  private String password;

  @Column(name = "register_time", length = 3)
  private Date registerTime;

  @Column(name = "register_ip", length = 50)
  private String registerIp;

  @Column(name = "status")
  @Convert(converter = UserStatus.Convert.class)
  private UserStatus status;

  public UserInfo() {}

  public UserInfo(String username, String password) {
    super();
    this.username = username;
    this.password = password;
  }

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
