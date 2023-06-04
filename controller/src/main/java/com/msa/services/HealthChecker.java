package com.msa.services;

import com.msa.deployment.Deployer;
import com.msa.models.Machine;
import com.msa.models.RunningInstance;
import com.msa.models.ServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@Component
public class HealthChecker {

    private final Deployer deployer;
    private final NodeMatcher nodeMatcher;

    @Value("${health-checker.cpu-threshold-percentage}")
    private String cpuThresholdPercentage;

    @Value("${health-checker.memory-threshold-percentage}")
    private String memoryThresholdPercentage;

    @Autowired
    public HealthChecker(Deployer deployer, NodeMatcher nodeMatcher) {
        this.nodeMatcher = nodeMatcher;
        this.deployer = deployer;
    }

    private void deployServiceToMatchedMachine(ServiceType serviceType) {
        Machine availableMachine = nodeMatcher.findNode(serviceType);
        if (availableMachine == null) {
            System.out.printf("No available machines to deploy %s%n", serviceType.getDirectory());
            return;
        }
        System.out.println("Migrate service to " + availableMachine.getIp());
        try {
            deployer.deployService(availableMachine, serviceType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkServiceHealth(Hashtable<String, String> metricResults, RunningInstance runningInstance) {
        System.out.println(metricResults);
        for (Map.Entry<String, String> metricResult : metricResults.entrySet()) {
            // check that the service is up
            switch (metricResult.getKey()) {
                case "status" -> {
                    if (metricResult.getValue().equals("0")) {
                        System.out.println("Service is down");
                        System.out.println("remove service from running instances");
                    }
                }
                case "memory" -> {
                    double usage = Double.parseDouble(metricResult.getValue()) / 10e9 / runningInstance.getHost().getMemory();
                    if (usage > Double.parseDouble(memoryThresholdPercentage)) {
                        System.out.println("Memory usage is above threshold");
                        deployServiceToMatchedMachine(runningInstance.getServiceType());
                    }
                }
                case "cpu" -> {
                    // check that the cpu usage is below the threshold
                    if (Double.parseDouble(metricResult.getValue()) > Double.parseDouble(cpuThresholdPercentage)) {
                        System.out.println("CPU usage is above threshold");
                        deployServiceToMatchedMachine(runningInstance.getServiceType());
                    }
                }
            }

        }
    }


    public void checkAllServicesAvailability(List<RunningInstance> runningInstances) {
        HashSet<ServiceType> availableServices = new HashSet<>();

        for (RunningInstance runningInstance : runningInstances) {
            availableServices.add(runningInstance.getServiceType());
        }

        for (ServiceType serviceType : ServiceType.values()) {
            if (!availableServices.contains(serviceType)) {
                // deploy a service of that type
                deployServiceToMatchedMachine(serviceType);
            }
        }
    }
}
