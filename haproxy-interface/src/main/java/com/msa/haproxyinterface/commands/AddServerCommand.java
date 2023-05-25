package com.msa.haproxyinterface.commands;

import com.msa.controller.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class AddServerCommand extends HAProxyCommand {

    private static final String command = "add server %s/%s %s:%s\n";

    @Override
    /*
     *  example json input:
     * {
     *   "service": "user-app",
     *   'instanceName": "app",
     *   "host": "172.54.126.2"
     *   "port": "5555"
     * }
     *   param json: json input
     * */
    public void send(OutputStream outputStream, HashMap<String, String> parameters) throws IOException {
        String service = parameters.get("appName");
        String instanceName = parameters.get("instanceId");
        String host = parameters.get("host");
        String port = parameters.get("port");
        byte[] bytes = String.format(command, service, instanceName, host, port).getBytes();
        Controller.logger.log(java.util.logging.Level.INFO, "Send command: " + new String(bytes));
        outputStream.write(bytes);
        outputStream.flush();
    }

    @Override
    public String handleCommandOutput(InputStream inputStream) throws IOException {
        return super.handleCommandOutput(inputStream);
    }



}
