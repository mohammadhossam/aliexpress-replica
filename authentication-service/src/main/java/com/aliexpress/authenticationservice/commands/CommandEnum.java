package com.aliexpress.authenticationservice.commands;

public enum CommandEnum {
    AuthenticateCommand("AuthenticateCommand", "authentication", "authentication.#");
    private final String name;
    private final String exchange;
    private final String routingKey;

    CommandEnum(String name, String exchange, String routingKey) {
        this.name = name;
        this.exchange = exchange;
        this.routingKey = routingKey;
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
