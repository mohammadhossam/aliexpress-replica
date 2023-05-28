package com.aliexpress.authenticationservice.commands;

import com.aliexpress.authenticationservice.models.Message;

public interface Command {
    public void execute(Message message);
}
