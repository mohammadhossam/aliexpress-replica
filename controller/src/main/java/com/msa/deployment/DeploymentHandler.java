package com.msa.deployment;

import lombok.AllArgsConstructor;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class DeploymentHandler {
    private final GitHandler gitHandler;
    private final SshHandler sshHandler;
    private final MavenHandler mavenHandler;


    public void runService(String hostUsername, String hostIp, String serviceName) throws GitAPIException, IOException {
        System.out.printf("Initializing run of service %s on %s@%s", serviceName, hostUsername, hostIp);
//        gitHandler.checkoutToBranch();
//        gitHandler.pull();

        mavenHandler.packageToJAR(serviceName);
        String pathToJar = mavenHandler.getPathToJar(serviceName);


        String fileNameOnServer = serviceName + ".jar";
        sshHandler.sendFileToServer(hostUsername, hostIp, pathToJar, fileNameOnServer);

        sshHandler.runCommandOnServer(hostUsername, hostIp, String.format("java -jar /home/shared/%s", fileNameOnServer));

        System.out.printf("Service %s is running on %s@%s", serviceName, hostUsername, hostIp);
    }

}
