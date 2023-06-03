package com.msa.deployment;

import com.msa.models.Machine;
import com.msa.models.RunningInstance;
import com.msa.models.ServiceType;
import com.msa.repos.RunningInstanceRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class Deployer {

    // when it gets a command to deploy a service, it will do the following
    // 1. find a machine to deploy the service on
    // 2. clone the repository of the service
    // 3. build the service jar
    // send the jar to the machine
    // 4. run the jar on the machine
    // 5. add the service to the database as a running service

    private final DeploymentHandler deploymentHandler;
    private final RunningInstanceRepo runningInstanceRepo;
    private final PrometheusHandler prometheusHandler;

    public void deployService(Machine machine, ServiceType serviceType) throws IOException {

        int port = deploymentHandler.runService(machine.getUsername(), machine.getIp(), serviceType.getDirectory());
        // add the service to the database as a running service
        RunningInstance recentDeployment = RunningInstance.builder().build();
        recentDeployment.setHost(machine);
        recentDeployment.setPort(Integer.toString(port));
        recentDeployment.setServiceType(serviceType);
        runningInstanceRepo.save(recentDeployment);

        // inform prometheus about the new service
        prometheusHandler.informPrometheus(recentDeployment);
    }

}
