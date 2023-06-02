package com.aliexpress.deploymenthandler;

import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;

public class DeploymentHandler {
    GitHandler gitHandler = new GitHandler();
    SshHandler sshHandler = new SshHandler();
    MavenHandler mavenHandler = new MavenHandler();

    public void runService(String hostUsername, String hostIp, String serviceName) throws GitAPIException, IOException {
        System.out.printf("Running service %s on %s@%s", serviceName, hostUsername, hostIp);
        gitHandler.checkoutToMaster();
        gitHandler.pull();

        mavenHandler.packageToJAR(serviceName);
        String pathToJar = mavenHandler.getPathToJar(serviceName);


        String fileNameOnServer = serviceName + ".jar";
        sshHandler.sendFileToServer(hostUsername, hostIp, pathToJar, fileNameOnServer);


        sshHandler.runCommandOnServer(hostUsername, hostIp, String.format("java -jar /home/shared/%s", fileNameOnServer));

        System.out.printf("Service %s is running on %s@%s", serviceName, hostUsername, hostIp);
    }

}
