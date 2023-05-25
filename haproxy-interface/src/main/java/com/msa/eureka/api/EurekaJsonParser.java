package com.msa.eureka.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.msa.service.Service;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;

public class EurekaJsonParser {

    private String jsonString;

    public EurekaJsonParser() {
    }

    public EurekaJsonParser(String json) {
        this.jsonString = json;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }

    public List<Service> parse() throws ParseException {
        JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        JsonArray appInstancesArrJson = jsonObject.getAsJsonObject("applications")
                .getAsJsonArray("application");

        List<Service> serviceList = new ArrayList<>();

        for (int i = 0; i < appInstancesArrJson.size(); i++) {
            JsonObject appJson = appInstancesArrJson.get(i).getAsJsonObject();
            String serviceName = appJson.get("name").getAsString().toLowerCase();

            JsonArray instancesArrJson = appJson.getAsJsonArray("instance");

            for (int j = 0; j < instancesArrJson.size(); j++) {
                JsonObject instanceJson = instancesArrJson.get(j).getAsJsonObject();
                String instanceId = instanceJson.get("instanceId").getAsString().toLowerCase();
                String host = instanceJson.get("ipAddr").getAsString();
                String port = instanceJson.getAsJsonObject("port").get("$").getAsString();

                Service service = new Service(serviceName, instanceId, host, port);
                serviceList.add(service);
            }
        }

        return serviceList;
    }
}
