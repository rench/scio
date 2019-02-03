package com.scio.cloud.stream.message.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
/**
 * Stream Message Model
 *
 * @author Wang.ch
 * @date 2019-02-01 10:22:36
 */
public class StreamMessage implements Serializable {

  /** */
  private static final long serialVersionUID = 6968158912531834204L;
  /** Message id */
  private Long id;
  /** Message type */
  private MessageType type;
  /** Message subType */
  private MessageSubType subType;
  /** Message from */
  private MessageFrom from;
  /** Message sendTo destinations */
  private List<String> destinations;
  /** Message template */
  private String template;
  /** Message parameters pairs */
  private List<StreamMessagePair> pairs;
  /** Message send source */
  private List<SendSource> sendSources;
  /** extend map */
  private Map<String, String> ext;
  /** @return the id */
  public Long getId() {
    return id;
  }

  /** @param id the id to set */
  public void setId(Long id) {
    this.id = id;
  }

  /** @return the type */
  public MessageType getType() {
    return type;
  }

  /** @param type the type to set */
  public void setType(MessageType type) {
    this.type = type;
  }

  /** @return the subType */
  public MessageSubType getSubType() {
    return subType;
  }

  /** @param subType the subType to set */
  public void setSubType(MessageSubType subType) {
    this.subType = subType;
  }

  /** @return the from */
  public MessageFrom getFrom() {
    return from;
  }

  /** @param from the from to set */
  public void setFrom(MessageFrom from) {
    this.from = from;
  }

  /** @return the template */
  public String getTemplate() {
    return template;
  }

  /** @param template the template to set */
  public void setTemplate(String template) {
    this.template = template;
  }

  /** @return the destinations */
  public List<String> getDestinations() {
    return destinations;
  }

  /** @param destinations the destinations to set */
  public void setDestinations(List<String> destinations) {
    this.destinations = destinations;
  }

  /** @return the pairs */
  public List<StreamMessagePair> getPairs() {
    return pairs;
  }

  /** @param pairs the pairs to set */
  public void setPairs(List<StreamMessagePair> pairs) {
    this.pairs = pairs;
  }

  /** @return the sendSources */
  public List<SendSource> getSendSources() {
    return sendSources;
  }

  /** @param sendSources the sendSources to set */
  public void setSendSources(List<SendSource> sendSources) {
    this.sendSources = sendSources;
  }

  /** @return the ext */
  public Map<String, String> getExt() {
    return ext;
  }

  /** @param ext the ext to set */
  public void setExt(Map<String, String> ext) {
    this.ext = ext;
  }

  public static class Builder {
    /** Message id */
    private Long id;
    /** Message type */
    private MessageType type;
    /** Message subType */
    private MessageSubType subType;
    /** Message from */
    private MessageFrom from;
    /** Message sendTo destinations */
    private List<String> destinations;
    /** Message template */
    private String template;
    /** Message parameters pairs */
    private List<StreamMessagePair> pairs;
    /** Message send source */
    private List<SendSource> sendSources;

    /** extend map */
    private Map<String, String> ext;

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder type(MessageType type) {
      this.type = type;
      return this;
    }

    public Builder subType(MessageSubType subType) {
      this.subType = subType;
      return this;
    }

    public Builder from(MessageFrom from) {
      this.from = from;
      return this;
    }

    public Builder template(String template) {
      this.template = template;
      return this;
    }

    public Builder destinations(String... destinations) {
      if (this.destinations == null) {
        this.destinations = new ArrayList<>();
      }
      this.destinations.addAll(Arrays.asList(destinations));
      return this;
    }

    public Builder pairs(StreamMessagePair... pairs) {
      if (this.pairs == null) {
        this.pairs = new ArrayList<>();
      }
      this.pairs.addAll(Arrays.asList(pairs));
      return this;
    }

    public Builder sendSources(SendSource... sendSources) {
      if (this.sendSources == null) {
        this.sendSources = new ArrayList<>();
      }
      this.sendSources.addAll(Arrays.asList(sendSources));
      return this;
    }

    public Builder ext(Map<String, String> ext) {
      if (this.ext == null) {
        this.ext = ext;
      } else {
        this.ext.putAll(ext);
      }
      return this;
    }

    public StreamMessage build() {
      StreamMessage msg = new StreamMessage();
      msg.setId(id);
      msg.setType(type);
      msg.setSubType(subType);
      msg.setFrom(from);
      msg.setDestinations(destinations);
      msg.setTemplate(template);
      msg.setPairs(pairs);
      msg.setSendSources(sendSources);
      return msg;
    }
  }

  /**
   * Message Type enum
   *
   * @author Wang.ch
   * @date 2019-02-01 10:24:14
   */
  public static enum MessageType {
    /** Order Message */
    ORDER,
    /** Payment Message */
    PAYMENT
  }
  /**
   * Message SubType enum
   *
   * @author Wang.ch
   * @date 2019-02-01 10:26:51
   */
  public static enum MessageSubType {
    /** Order create */
    ORDER_CREATE,
    /** Order cancel */
    ORDER_CANCEL,
    /** Order finish */
    ORDER_FINISH,
    /** Order refund */
    ORDER_REFUND,
    /** Payment success */
    PAYMENT_SUCCESS,
    /** Payment fail */
    PAYMENT_FAIL
  }
  /**
   * Message From
   *
   * @author Wang.ch
   * @date 2019-02-01 10:30:01
   */
  public static enum MessageFrom {
    /** Store Service */
    STORE,
    /** Provider Service */
    PROVIDER,
    /** Wallet Service */
    WALLET
  }

  /**
   * Send source
   *
   * @author Wang.ch
   * @date 2019-02-01 10:24:09
   */
  public static enum SendSource {
    /** WECHAT Template Message */
    WECHAT,
    /** SMS */
    SMS,
    /** APP notification */
    APP_NOTIFY
  }
}
