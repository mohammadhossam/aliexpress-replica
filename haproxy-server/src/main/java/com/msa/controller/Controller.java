package com.msa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.msa.eureka.api.EurekaClient;
import com.msa.haproxyclient.commands.AddServerCommand;
import com.msa.haproxyclient.commands.EnableServerCommand;
import com.msa.haproxyclient.commands.HAProxyCommand;
import com.msa.haproxyclient.HaproxyClient;
import com.msa.service.Service;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {
    public static Logger logger = Logger.getLogger(Controller.class.getName());
    private HaproxyClient haproxyClient;
    private EurekaClient eurekaClient;
    private ServiceInfoCache<Service> serviceInfoCache = new ServiceInfoCache<>();

    public Controller() throws IOException {
        String proxyHost = Properties.getProperties().getProperty("haproxy.host");
        String proxyPort = (Properties.getProperties().getProperty("haproxy.port"));
        haproxyClient = new HaproxyClient();
        haproxyClient.connect(proxyHost, proxyPort);
        eurekaClient = new EurekaClient();
        serviceInfoCache = new ServiceInfoCache<>();
    }

    public void updateHaproxy() throws IOException, ParseException, InterruptedException {
        List<Service> serviceList = eurekaClient.getAvailableServices();
        HAProxyCommand addServerCommand = new AddServerCommand();
        HAProxyCommand enableServerCommand = new EnableServerCommand();
        String host = Properties.getProperties().getProperty("haproxy.host");
        String port = Properties.getProperties().getProperty("haproxy.port");
        for (Service service : serviceList) {
            if (!serviceInfoCache.isServiceAvailable(service)) {
                haproxyClient.connect(host, port);
                logger.log(Level.INFO, "Adding service " + service);
                String response = haproxyClient.sendCommand(addServerCommand, service.toMap());
                logger.log(Level.INFO, "Response: " + response);
                serviceInfoCache.add(service);
                haproxyClient.disconnect();
                haproxyClient.connect(host, port);
                logger.log(Level.INFO, "Enabling service " + service);
                haproxyClient.sendCommand(enableServerCommand, service.toMap());
                haproxyClient.disconnect();
            }
        }
        System.out.println(serviceInfoCache.serviceCache);
    }


}
