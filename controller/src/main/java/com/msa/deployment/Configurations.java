package com.msa.deployment;

import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.Invoker;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class Configurations {
    @Value("${gitToken}")
    private String gitToken;

    @Value("${localRepoPath}")
    private String localPath;

    @Bean
    public Invoker getDefaultInvoker() {
        return new DefaultInvoker();
    }

    @Bean
    public CredentialsProvider getCredentialsProvider() {
        return new UsernamePasswordCredentialsProvider(gitToken, "");
    }

    @Bean
    public Git getGit() {
        Repository localRepo;
        try {
            localRepo = new FileRepository(localPath + "/.git");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Git(localRepo);
    }

}
