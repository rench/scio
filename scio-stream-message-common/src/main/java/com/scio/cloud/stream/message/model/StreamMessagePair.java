package com.scio.cloud.stream.message.model;

import java.io.Serializable;
/**
 * Stream Message Parameters Pair
 *
 * @author Wang.ch
 * @date 2019-02-01 10:32:16
 */
public class StreamMessagePair implements Serializable {

  /** */
  private static final long serialVersionUID = -1506265779154870094L;

  /** Parameter key */
  private String key;
  /** Parameter value */
  private String value;
  /** @return the key */
  public String getKey() {
    return key;
  }
  /** @param key the key to set */
  public void setKey(String key) {
    this.key = key;
  }
  /** @return the value */
  public String getValue() {
    return value;
  }
  /** @param value the value to set */
  public void setValue(String value) {
    this.value = value;
  }
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return String.format("MessagePair [key=%s, value=%s]", key, value);
  }
}
