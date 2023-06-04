package com.msa.deployment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.msa.models.prometheus.PrometheusYml;
import com.msa.models.RunningInstance;
import com.msa.models.prometheus.ScrapeConfig;
import com.msa.models.prometheus.StaticConfig;
import com.msa.services.MetricsPuller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.io.*;
import java.util.Vector;


@Component
public class PrometheusHandler {

    @Value("${prometheusYamlFile}")
    private String prometheusYamlPath;

    private final MetricsPuller metricsPuller;

    public PrometheusHandler(MetricsPuller metricsPuller) {
        this.metricsPuller = metricsPuller;
    }

    public void informPrometheus(RunningInstance runningInstance) {
        try {

            // open the prometheus .yml file

            File file = new File(prometheusYamlPath);

            // Create ObjectMapper with YAMLFactory
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));

            // Read YAML file and parse into Config object
            PrometheusYml config = objectMapper.readValue(file, PrometheusYml.class);

            System.out.println(runningInstance);
            System.out.println(config);

            // Add new target to yaml
            ScrapeConfig runningInstanceConfig = null;
            for (ScrapeConfig scrapeConfig : config.getScrape_configs()) {
                if (scrapeConfig.getJob_name().equals(runningInstance.getServiceType().getDirectory())) {
                    runningInstanceConfig = scrapeConfig;
                    break;
                }
            }
            if (runningInstanceConfig == null) {
                runningInstanceConfig = new ScrapeConfig();
                runningInstanceConfig.setJob_name(runningInstance.getServiceType().getDirectory());
                runningInstanceConfig.setMetrics_path("/actuator/prometheus");
                runningInstanceConfig.setStatic_configs(new Vector<>());
                runningInstanceConfig.getStatic_configs().add(new StaticConfig(new Vector<>()));

                config.getScrape_configs().add(runningInstanceConfig);
            }
            if (runningInstanceConfig.getStatic_configs().isEmpty()) {
                runningInstanceConfig.getStatic_configs().add(new StaticConfig());
            }
            StaticConfig staticConfig = runningInstanceConfig.getStatic_configs().get(0);
            staticConfig.getTargets().add(runningInstance.getHost().getIp() + ":" + runningInstance.getPort());


            objectMapper.writeValue(file, config);

            metricsPuller.reloadYaml();

            System.out.println(config);

            System.out.println("Target added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
