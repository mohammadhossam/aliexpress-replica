package com.aliexpress.usersservice.usersservice.models.Command;

public enum CommandEnum {
    AuthenticateCommand("AuthenticateCommand", "user.#", "user");
    private final String name;
    private final String routingKey;
    private final String exchange;

    CommandEnum(String name, String routingKey, String exchange) {
        this.name = name;
        this.routingKey = routingKey;
        this.exchange = exchange;
    }

    public String getName() {
        return name;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public String getExchange() {
        return exchange;
    }
}