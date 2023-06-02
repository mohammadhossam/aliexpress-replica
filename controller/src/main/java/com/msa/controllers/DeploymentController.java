package com.msa.controllers;

import com.msa.deployment.Deployer;
import com.msa.models.Machine;
import com.msa.models.requests.DeploymentRequest;
import com.msa.models.responses.DeploymentResponse;
import com.msa.services.NodeMatcher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/deployment")
@AllArgsConstructor
public class DeploymentController {

    private final Deployer deployer;
    private final NodeMatcher nodeMatcher;

    @PostMapping()
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
}
