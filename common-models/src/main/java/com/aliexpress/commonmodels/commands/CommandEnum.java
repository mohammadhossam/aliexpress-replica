package com.aliexpress.commonmodels.commands;

public enum CommandEnum {
    DecrementInventoryCommand("com.aliexpress.inventoryservice.models.commands.DecrementInventoryCommand"),
    IncrementInventoryCommand("com.aliexpress.inventoryservice.models.commands.IncrementInventoryCommand"),
    CreateInventoryCommand("com.aliexpress.inventoryservice.models.commands.CreateInventoryCommand"),
    DeleteInventoryCommand("com.aliexpress.inventoryservice.models.commands.DeleteInventoryCommand"),
    PayToMerchantCommand("com.aliexpress.paymentservice.models.commands.PayToMerchantCommand"),
    DeleteOrderCommand("com.aliexpress.orderservice.models.commands.DeleteOrderCommand");
    private final String name;

    CommandEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}