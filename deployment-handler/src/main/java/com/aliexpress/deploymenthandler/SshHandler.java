package com.aliexpress.deploymenthandler;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.Channel;
import org.apache.sshd.sftp.client.fs.SftpFileSystemProvider;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;

public class SshHandler {

    public void sendFileToServer(String serverUsername, String serverIp, String localPath, String fileNameOnServer) throws IOException {
        System.out.printf("Sending file from %s on local machine to /shared/%s %s@%s%n", localPath, fileNameOnServer, serverUsername, serverIp);
        SshClient client = SshClient.setUpDefaultClient();
        client.start();
        ClientSession session = client.connect(serverUsername, serverIp, 22).verify().getClientSession();
        URI uri = SftpFileSystemProvider.createFileSystemURI(serverIp, 22, "sftp_user", "aliexpress");

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
}
