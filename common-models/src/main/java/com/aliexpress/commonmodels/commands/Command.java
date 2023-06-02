package com.aliexpress.commonmodels.commands;

import com.aliexpress.commonmodels.Message;

public interface Command {
    public void execute(Message message);
}