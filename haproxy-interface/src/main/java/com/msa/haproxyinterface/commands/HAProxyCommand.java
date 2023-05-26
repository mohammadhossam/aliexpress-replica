package com.msa.haproxyinterface.commands;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class HAProxyCommand {
    public abstract void send(OutputStream outputStream, HashMap<String, String> parameters) throws IOException;

    public HashMap<String, String> parseJson(String jsonInput) {
        Map<String, String> retMap = new Gson().fromJson(
                jsonInput, new TypeToken<HashMap<String, String>>() {
                }.getType()
        );
        return new HashMap<>(retMap);
    }

    public String handleCommandOutput(InputStream inputStream) throws IOException {
        StringBuilder response = new StringBuilder();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            response.append(new String(buffer, 0, bytesRead));
        }

        return response.toString();
    }

}
