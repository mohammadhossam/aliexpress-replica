package com.aliexpress.usersservice.usersservice.models;

import com.aliexpress.usersservice.usersservice.models.Command.CommandEnum;
import lombok.*;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@ToString
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