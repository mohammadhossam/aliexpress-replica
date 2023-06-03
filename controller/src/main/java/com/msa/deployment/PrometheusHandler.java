package com.msa.deployment;

import com.msa.models.Query;
import com.msa.models.RunningInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.*;
import java.util.*;
@Component
public class PrometheusHandler {

    private final ResourceLoader resourceLoader;

    @Autowired
    public PrometheusHandler(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    public void informPrometheus(RunningInstance runningInstance) {
        try {

            // open the prometheus .yml file
            Resource resource = resourceLoader.getResource("classpath:prometheus.yml");
            InputStream file = resource.getInputStream();

            // Load the YAML file
            Yaml yaml = new Yaml();
            Map yamlData = yaml.load(file);

            // Get the list of current targets
            List<Map<String, Object>> jobs = ((List<Map<String, Object>>)yamlData.get("scrape_configs"));
            System.out.println(jobs);
//            for (Map<String, Object> job: jobs) {
//
//            }
//
//            // Add the new target
//            List<String> newTarget = new ArrayList<>();
//            newTarget.add(targetName);
//            newTarget.add(targetURL);
//            targets.add(newTarget);
//
//            // Save the updated YAML file
//            FileWriter writer = new FileWriter(filePath);
//            yaml.dump(yamlData, writer);
//            writer.close();

            System.out.println("Target added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
