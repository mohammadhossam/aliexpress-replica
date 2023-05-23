package com.msa.haproxyclient;

import com.msa.controller.Controller;
import com.msa.haproxyclient.commands.HAProxyCommand;
import com.msa.haproxyclient.commands.HAProxyCommandRegistry;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;

public class HaproxyClient {
    private Socket socket;
    private InputStream input;
    private OutputStream output;

    public void connect(String host, String port) throws IOException {
        socket = new Socket(host, Integer.parseInt(port));
        input = socket.getInputStream();
        output = socket.getOutputStream();
//        initConnection();
    }

    private void initConnection() throws IOException {
        HAProxyCommand promptCommand = HAProxyCommandRegistry.getCommand("prompt");
        Controller.logger.log(Level.INFO, "Initializing connection");
        promptCommand.send(output, null);
        Controller.logger.log(Level.INFO, promptCommand.handleCommandOutput(input));
        Controller.logger.log(Level.INFO, "Connection initialized");
    }

    public void disconnect() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    public String sendCommand(HAProxyCommand haProxyCommand, HashMap<String, String> parameters) throws IOException {
        haProxyCommand.send(output, parameters);
        return haProxyCommand.handleCommandOutput(input);
    }


}
