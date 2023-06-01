package com.msa.model;

public enum Command {
  CreateInventoryCommand("CreateInventoryCommand"),
  DeleteInventoryCommand("DeleteInventoryCommand");

  private final String name;

  Command(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

}