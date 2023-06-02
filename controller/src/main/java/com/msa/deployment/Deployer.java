package com.msa.deployment;

import org.springframework.stereotype.Component;

@Component
public class Deployer {

    // when it gets a command to deploy a service, it will do the following
    // 1. find a machine to deploy the service on
    // 2. clone the repository of the service
    // 3. build the service jar
    // send the jar to the machine
    // 4. run the jar on the machine
    // 5. add the service to the database as a running service

    public void deployService () {

    }

}
