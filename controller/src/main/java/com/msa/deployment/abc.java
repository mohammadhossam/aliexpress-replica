package com.msa.deployment;

import lombok.AllArgsConstructor;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@AllArgsConstructor
@RequestMapping("/abc")
public class abc {
    private final DeploymentHandler dh;

    @GetMapping()
    public void run() {
        try {
            dh.runService("yahia","192.168.1.27","discovery-server");
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
