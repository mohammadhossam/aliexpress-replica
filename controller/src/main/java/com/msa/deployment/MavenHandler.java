package com.msa.deployment;

import org.apache.maven.shared.invoker.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collections;

@Component
public class MavenHandler {

    @Value("${mavenHome}")
    private String mavenHome;
    @Value("${localRepoPath}")
    private String localRepoPath;
    @Value("${version}")
    private String version;

    private final Invoker invoker;

    @Autowired
    public MavenHandler(Invoker invoker) {
        this.invoker = invoker;
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
        String pathToJar = String.format(localRepoPath + "\\%s\\target\\%s-%s.jar", serviceName, serviceName, version);
        System.out.printf("Path to Jar is %s%n", pathToJar);
        return pathToJar;
    }

}
