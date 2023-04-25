package alibaba.controller;

import org.springframework.stereotype.Component;

@Component
public class ServiceCheckHandler {

    public void handleMessage (String message, String sender) {
        System.out.println("Received message: " + message);
    }
}
