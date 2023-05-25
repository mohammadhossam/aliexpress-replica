package com.msa.eureka.api;

import com.msa.service.Service;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;

public class EurekaClient {
    private EurekaAPI eurekaAPI;
    private EurekaJsonParser eurekaJsonParser;

    public EurekaClient() throws IOException {
        eurekaAPI = new EurekaAPI();
        eurekaJsonParser = new EurekaJsonParser();
    }

    public List<Service> getAvailableServices() throws IOException, ParseException {
        String json = eurekaAPI.getAllInstances();
        eurekaJsonParser.setJsonString(json);
        return eurekaJsonParser.parse();
    }
}
