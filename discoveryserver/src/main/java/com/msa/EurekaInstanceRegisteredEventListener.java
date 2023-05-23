package com.msa;

import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EurekaInstanceRegisteredEventListener {

    @EventListener
    public void handleEurekaInstanceRegisteredEvent(EurekaInstanceRegisteredEvent event) {
        // Perform actions when a new instance is registered
        // For example, you can log the event or trigger a custom event
        System.out.println("[New instance registered]: " + event.getInstanceInfo().getAppName());
        System.out.println("[New instance registered]: id = " + event.getInstanceInfo().getId());
    }

    @EventListener
    public void handleEurekaInstanceCanclledEvent(EurekaInstanceCanceledEvent event) {
        // Perform actions when a new instance is registered
        // For example, you can log the event or trigger a custom event
        System.out.println("[An instance is unregistered]: " + event.getAppName());
    }

}
