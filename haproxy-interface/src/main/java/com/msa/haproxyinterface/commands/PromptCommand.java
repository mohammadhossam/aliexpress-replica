package com.msa.haproxyinterface.commands;

import java.io.*;
import java.util.HashMap;

public class PromptCommand extends HAProxyCommand {
    @Override
    public void send(OutputStream outputStream, HashMap<String, String> parameters) throws IOException {
        outputStream.write("prompt\n".getBytes());
        outputStream.flush();
    }

    @Override
    public String handleCommandOutput(InputStream inputStream) throws IOException {
        return "prompt";
    }
}
