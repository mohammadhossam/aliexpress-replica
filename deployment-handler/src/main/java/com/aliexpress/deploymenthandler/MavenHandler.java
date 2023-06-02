package com.aliexpress.deploymenthandler;

import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.util.Collections;

public class MavenHandler {
    String mavenHome = "C:\\Program Files\\Maven\\apache-maven-3.9.2";
    String localRepoPath = "C:\\Users\\yahia\\aliexpress-replica";
    Invoker invoker = new DefaultInvoker();

    public MavenHandler() {

    }

    public void packageToJAR(String serviceName) {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setMavenHome(new File(mavenHome));
        request.setPomFile(new File(String.format(localRepoPath + "\\%s\\pom.xml", serviceName)));
        request.setGoals(Collections.singletonList("package"));

        InvocationResult result;
        try {
            result = invoker.execute(request);
        } catch (MavenInvocationException e) {
            throw new RuntimeException(e);
        }
        if (result.getExitCode() != 0) {
            throw new IllegalStateException("Build failed.");
        }
        System.out.printf("Packaged %s to JAR%n", serviceName);
    }

    public String getPathToJar(String serviceName) {
        String pathToJar = String.format(localRepoPath + "\\%s\\target\\%s-1.0-SNAPSHOT.jar", serviceName, serviceName);
        System.out.printf("Path to Jar is %s%n", pathToJar);
        return pathToJar;
    }

}
