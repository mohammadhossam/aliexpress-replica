package com.msa.deployment;

import lombok.AllArgsConstructor;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Component
@AllArgsConstructor
public class GitHandler {


    @Value("${branchName}")
    private String branchName;
    private final Git git;
    private final CredentialsProvider credentialsProvider;

    @Autowired
    public GitHandler(Git git, CredentialsProvider credentialsProvider) {
        this.git = git;
        this.credentialsProvider = credentialsProvider;
    }


    public void checkoutToBranch() {
        for (int i = 0; i < 3; i++) {
            try {
                CheckoutCommand checkoutCommand = git.checkout().setName(branchName);
                checkoutCommand.call();
                System.out.printf("Checked out to %s branch%n", branchName);
                return;
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
    }

    public void pull() {
        for (int i = 0; i < 3; i++) {
            try {
                PullCommand pullCmd = git.pull().setCredentialsProvider(credentialsProvider);
                pullCmd.call();
                System.out.printf("Pulled remote%n");
                return;
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
        }
    }

}

