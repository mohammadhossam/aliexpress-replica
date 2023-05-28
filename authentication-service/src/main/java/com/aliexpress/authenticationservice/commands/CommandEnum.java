package com.aliexpress.authenticationservice.commands;

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
