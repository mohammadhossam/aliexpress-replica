package com.msa.haproxyinterface.commands;

import java.util.HashMap;

public class HAProxyCommandRegistry {
    private static HashMap<String, HAProxyCommand> registry = new HashMap<>();

    static {
        registry.put("add server", new AddServerCommand());
        registry.put("prompt", new PromptCommand());
    }

    public static HAProxyCommand getCommand(String command) {
        return registry.get(command);
    }
}
