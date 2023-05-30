package com.aliexpress.commonmodels.commands;

public enum CommandEnum {
    DecrementInventoryCommand("com.aliexpress.inventoryservice.models.commands.DecrementInventoryCommand"),
    IncrementInventoryCommand("com.aliexpress.inventoryservice.models.commands.IncrementInventoryCommand"),
    PayToMerchantCommand("com.aliexpress.paymentservice.models.commands.PayToMerchantCommand");
    private final String name;

    CommandEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}