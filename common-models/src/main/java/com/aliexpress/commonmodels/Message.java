package com.aliexpress.commonmodels;

import com.aliexpress.commonmodels.commands.CommandEnum;
import lombok.*;

import java.util.Date;
import java.util.Map;

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
    private CommandEnum command;
    private Map<String, Object> dataMap;
    private  String source;
}