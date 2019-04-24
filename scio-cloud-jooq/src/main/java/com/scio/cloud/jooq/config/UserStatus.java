package com.scio.cloud.jooq.config;

import java.util.Optional;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * user status
 *
 * @author Wang.ch
 */
public enum UserStatus {
  /** normal */
  NORMAL(1, "normal"),
  /** locked */
  LOCKED(2, "locked"),
  /** cancellation */
  CANCELLATION(3, "cancellation");
  private Integer value;
  private String name;

  private UserStatus(Integer value, String name) {
    this.value = value;
    this.name = name;
  }

  @JsonValue
  public Integer getValue() {
    return value;
  }

  public String getName() {
    return name;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

  public void setName(String name) {
    this.name = name;
  }

  @JsonCreator
  public static UserStatus forValue(Integer value) {
    UserStatus[] array = UserStatus.values();
    for (UserStatus obj : array) {
      if (obj.getValue().equals(value)) {
        return obj;
      }
    }
    return null;
  }
  /**
   * Jpa enum convert class
   *
   * @author Wang.ch
   * @date 2019-04-15 09:29:37
   */
  public static class Convert implements AttributeConverter<UserStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(UserStatus attribute) {
      return Optional.ofNullable(attribute).map(UserStatus::getValue).orElse(null);
    }

    @Override
    public UserStatus convertToEntityAttribute(Integer dbData) {
      if (dbData == null) {
        return null;
      }
      UserStatus[] array = UserStatus.values();
      for (UserStatus obj : array) {
        if (obj.getValue().equals(dbData)) {
          return obj;
        }
      }
      return null;
    }
  }
}
