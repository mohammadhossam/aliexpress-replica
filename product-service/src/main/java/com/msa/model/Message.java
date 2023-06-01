package com.msa.model;

import lombok.*;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Message {
  private String messageId;
  private Date messageDate;
  private String exchange;
  private String routingKey;
  private Command command;
  private Map<String, Object> dataMap;
  private String source;

  public Message(Command command, Map<String, Object> dataMap, String source, String exchange, String routingKey) {
    this.messageId = UUID.randomUUID().toString();
    this.messageDate = new Date();
    this.command = command;
    this.dataMap = dataMap;
    this.exchange = exchange;
    this.routingKey = routingKey;
    this.source = source;
  }

  public void generateMessageId() {
    this.messageId = UUID.randomUUID().toString();
  }
}