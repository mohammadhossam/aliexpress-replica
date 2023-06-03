package com.msa.deployment;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.Channel;
import org.apache.sshd.sftp.client.fs.SftpFileSystemProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;

@Component
public class SshHandler {

    @Value("${sftpUser}")
    String sftpUsername;

    @Value("${sftpPassword}")
    String sftpPassword;

    public void sendFileToServer(String serverUsername, String serverIp, String localPath, String fileNameOnServer) throws IOException {
        System.out.printf("Sending file from %s on local machine to /shared/%s %s@%s%n", localPath, fileNameOnServer, serverUsername, serverIp);
        SshClient client = SshClient.setUpDefaultClient();
        client.start();

        URI uri = SftpFileSystemProvider.createFileSystemURI(serverIp, 22, sftpUsername, sftpPassword);

        SftpFileSystemProvider provider = new SftpFileSystemProvider(client);
        FileSystem fs = provider.newFileSystem(uri, Collections.<String, Object>emptyMap());

        Path remotePath = fs.getPath("/shared/" + fileNameOnServer);

        Files.copy(new File(localPath).toPath(), remotePath, StandardCopyOption.REPLACE_EXISTING);

        System.out.printf("File sent from %s on local machine to /shared/%s %s@%s%n", localPath, fileNameOnServer, serverUsername, serverIp);
    }

    public void runCommandOnServer(String serverUsername, String serverIp, String command) throws IOException {
        System.out.printf("Running command : %s on %s@%s%n", command, serverUsername, serverIp);
        SshClient client = SshClient.setUpDefaultClient();
        client.start();
        ClientSession session = client.connect(serverUsername, serverIp, 22).verify().getClientSession();
        session.auth().verify(5000);


        ClientChannel channel = session.createChannel(Channel.CHANNEL_EXEC, command);


        channel.open().verify();

        channel.close();

        System.out.printf("command : %s , run on %s@%s%n", command, serverUsername, serverIp);
    }

    public int getRandomOpenPort(String serverUsername, String serverIp) throws IOException {
        System.out.printf("Getting random port on %s@%s%n", serverUsername, serverIp);
        SshClient client = SshClient.setUpDefaultClient();
        client.start();
        ClientSession session = client.connect(serverUsername, serverIp, 22).verify().getClientSession();
        session.auth().verify(5000);

        int startRange = 3000;
        int endRange = 20000;
        int numberOfPorts = 1;
        String command = String.format("comm -23 <(seq %d %d | sort) <(ss -Htan | awk '{print $4}' | cut -d':' -f2 | sort -u) | shuf | head -n %d", startRange, endRange, numberOfPorts);
        String output = session.executeRemoteCommand(command);

        System.out.printf("Running command to get random port : %s on %s@%s%n", command, serverUsername, serverIp);

        int randomPort = Integer.parseInt(output.replaceAll("[^\\d.]", ""));

        System.out.printf("Random port %d  on %s@%s%n", randomPort, serverUsername, serverIp);
        return randomPort;
    }
}
