package com.msa.haproxyclient.commands;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class EnableServerCommand extends HAProxyCommand {

    public static final String COMMAND_NAME = "enable server %s/%s state ready\n";

    @Override
    public void send(OutputStream outputStream, HashMap<String, String> parameters) throws IOException {
        String appName = parameters.get("appName");
        String instanceId = parameters.get("instanceId");
        String command = String.format(COMMAND_NAME, appName, instanceId);
        outputStream.write(command.getBytes());
        outputStream.flush();
    }

    public String handleCommand() {
        return "the server is enabled";
    }


}
