package com.aliexpress.deploymenthandler;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class GitHandler {
    String localPath = "C:\\Users\\yahia\\aliexpress-replica";
    String remotePath = "https://github.com/mohammadhossam/aliexpress-replica.git";
    Repository localRepo;
    Git git;

    Properties p;
    CredentialsProvider credentialsProvider;

    public GitHandler() {
        FileReader reader;
        try {
            reader = new FileReader("deployment-handler/src/main/resources/application.properties");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        p = new Properties();
        try {
            p.load(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        credentialsProvider = new UsernamePasswordCredentialsProvider(p.getProperty("gittoken"), "");
        try {
            localRepo = new FileRepository(localPath + "/.git");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        git = new Git(localRepo);
    }


    public void checkoutToMaster() throws GitAPIException {
        CheckoutCommand checkoutCommand = git.checkout().setName("master");
        checkoutCommand.call();
        System.out.printf("Checked out to master branch%n");
    }

    public void pull() throws GitAPIException {
        PullCommand pullCmd = git.pull().setCredentialsProvider(credentialsProvider);
        pullCmd.call();
        System.out.printf("Pulled remote%n");
    }

}

