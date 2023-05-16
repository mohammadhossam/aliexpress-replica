package com.eureka.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.service.Service;
import com.service.ServiceInstance;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EurekaJsonParser {

    private String jsonString;

    public EurekaJsonParser(String json) {
        this.jsonString = json;
    }

    public void parse() throws ParseException {
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        JsonArray appInstancesArrJson = jsonObject.getAsJsonObject("applications")
                .getAsJsonArray("application");

        List<Service> serviceList = new ArrayList<>();

        for (int i = 0; i < appInstancesArrJson.size(); i++) {
            JsonObject appJson = appInstancesArrJson.get(i).getAsJsonObject();
            String serviceName = appJson.get("name").getAsString();
            List<ServiceInstance> instances = new ArrayList<>();

            JsonArray instancesArrJson = appJson.getAsJsonArray("instance");

            for (int j = 0; j < instancesArrJson.size(); j++) {
                JsonObject instanceJson = instancesArrJson.get(j).getAsJsonObject();

                String host = instanceJson.get("hostName").getAsString();
                String port = instanceJson.getAsJsonObject("port").get("$").getAsString();

                ServiceInstance instance = new ServiceInstance(serviceName, host, port);
                instances.add(instance);
            }

            Service service = new Service(serviceName, instances);
            serviceList.add(service);
        }

        for (Service service : serviceList) {
            System.out.println(service);
        }
    }

    public static void main(String[] args) throws ParseException, IOException {
        EurekaAPI api = new EurekaAPI();
        String ans = api.getAllInstances();
        EurekaJsonParser parser = new EurekaJsonParser(ans);
        parser.parse();
    }
}
