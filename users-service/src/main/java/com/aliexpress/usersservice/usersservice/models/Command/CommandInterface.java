package com.aliexpress.usersservice.usersservice.models.Command;

import com.aliexpress.usersservice.usersservice.models.Message;

public interface CommandInterface {
    public void execute(Message message);

}