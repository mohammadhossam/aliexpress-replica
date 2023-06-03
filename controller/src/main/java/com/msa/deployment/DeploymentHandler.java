package com.msa.deployment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@AllArgsConstructor
public class DeploymentHandler {
    private final GitHandler gitHandler;
    private final SshHandler sshHandler;
    private final MavenHandler mavenHandler;


    public int runService(String hostUsername, String hostIp, String serviceName) throws IOException {
        System.out.printf("Initializing run of service %s on %s@%s%n", serviceName, hostUsername, hostIp);
        gitHandler.checkoutToBranch();
        gitHandler.pull();

        mavenHandler.packageToJAR(serviceName);
        String pathToJar = mavenHandler.getPathToJar(serviceName);


        String fileNameOnServer = serviceName + ".jar";
        sshHandler.sendFileToServer(hostUsername, hostIp, pathToJar, fileNameOnServer);

        int randomPort = sshHandler.getRandomOpenPort(hostUsername, hostIp);
        sshHandler.runCommandOnServer(hostUsername, hostIp, String.format("java -Dserver.port=%d -jar /home/shared/%s", randomPort, fileNameOnServer));

        System.out.printf("Service %s is running on %s@%s port:%d%n", serviceName, hostUsername, hostIp, randomPort);
        return randomPort;
    }

}
