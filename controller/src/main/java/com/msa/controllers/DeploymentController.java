package com.msa.controllers;

import com.msa.deployment.Deployer;
import com.msa.models.Machine;
import com.msa.models.Property;
import com.msa.models.ServiceConfiguration;
import com.msa.models.requests.DeploymentRequest;
import com.msa.models.responses.DeploymentResponse;
import com.msa.repos.ServiceConfigurationRepo;
import com.msa.services.NodeMatcher;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/deployment")
@AllArgsConstructor
public class DeploymentController {

    private final Deployer deployer;
    private final NodeMatcher nodeMatcher;
    private final ServiceConfigurationRepo serviceConfigurationRepo;;

    @PostMapping("/deployInstance")
    public DeploymentResponse deploy (@RequestBody DeploymentRequest deploymentRequest) {

        Machine availableMachine = nodeMatcher.findNode(deploymentRequest.getServiceType());
        try {
            deployer.deployService(availableMachine, deploymentRequest.getServiceType());
        }
        catch (Exception e) {
            e.printStackTrace();
            return DeploymentResponse.builder().build();
        }

        DeploymentResponse response = DeploymentResponse.builder().build();
        response.setMachine(availableMachine);
        return response;
    }

    @GetMapping("/add")
    public String addNode() {
        ServiceConfiguration configuration = new ServiceConfiguration();

        configuration.setServiceName("user-service");
        ArrayList<Property> properties = new ArrayList<>();
        properties.add(new Property( "spring.data.mongodb.host", "{db.ip}"));
        properties.add(new Property( "spring.data.mongodb.port", "{db.port}"));
        configuration.setApplicationProperties(properties);
        serviceConfigurationRepo.save(configuration);
        return "add";
    }
}
