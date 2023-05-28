package com.aliexpress.usersservice.usersservice.models.Command;

public enum CommandEnum {
    AuthenticateCommand("AuthenticateCommand");
    private final String name;

    CommandEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}