package com.msa.controller;

import com.msa.eureka.api.EurekaClient;
import com.msa.haproxyinterface.commands.AddServerCommand;
import com.msa.haproxyinterface.commands.EnableServerCommand;
import com.msa.haproxyinterface.commands.HAProxyCommand;
import com.msa.haproxyinterface.HaproxyInterface;
import com.msa.service.Service;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {
    public static Logger logger = Logger.getLogger(Controller.class.getName());
    private HaproxyInterface haproxyInterface;
    private EurekaClient eurekaClient;
    private ServiceInfoCache<Service> serviceInfoCache = new ServiceInfoCache<>();

    public Controller() throws IOException {
        String proxyHost = Properties.getProperties().getProperty("haproxy.host");
        String proxyPort = (Properties.getProperties().getProperty("haproxy.port"));
        haproxyInterface = new HaproxyInterface();
        haproxyInterface.connect(proxyHost, proxyPort);
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
                haproxyInterface.connect(host, port);
                logger.log(Level.INFO, "Adding service " + service);
                String response = haproxyInterface.sendCommand(addServerCommand, service.toMap());
                logger.log(Level.INFO, "Response: " + response);
                serviceInfoCache.add(service);
                haproxyInterface.disconnect();
                haproxyInterface.connect(host, port);
                logger.log(Level.INFO, "Enabling service " + service);
                haproxyInterface.sendCommand(enableServerCommand, service.toMap());
                haproxyInterface.disconnect();
            }
        }
        System.out.println(serviceInfoCache.serviceCache);
    }


}
