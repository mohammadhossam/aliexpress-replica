package com.aliexpress.authenticationservice.models;

import com.aliexpress.authenticationservice.commands.CommandEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@ToString
@Builder
public class Message {
    private final String messageId;
    private final Date messageDate;
    private final CommandEnum command;
    private final Map<String, Object> dataMap;
    private final String source;

    public Message(CommandEnum command, Map<String, Object> dataMap, String source) {
        this.messageId = UUID.randomUUID().toString();
        this.messageDate = new Date();
        this.command = command;
        this.dataMap = dataMap;
        this.source = source;
    }

    public String getRoutingKey() {
        return command.getRoutingKey();
    }
}
