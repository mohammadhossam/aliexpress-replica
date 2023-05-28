package com.aliexpress.authenticationservice.models;

import com.aliexpress.authenticationservice.commands.CommandEnum;
import lombok.*;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Message {
    private  String messageId;
    private  Date messageDate;
    private String exchange;
    private String routingKey;
    private  CommandEnum command;
    private  Map<String, Object> dataMap;
    private  String source;


    public Message(CommandEnum command, Map<String, Object> dataMap, String source, String exchange, String routingKey ) {
        this.messageId = UUID.randomUUID().toString();
        this.messageDate = new Date();
        this.command = command;
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.dataMap = dataMap;
        this.source = source;
    }

}
