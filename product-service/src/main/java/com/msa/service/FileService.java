package com.msa.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.msa.adapters.AmazonS3FileClientAdapter;
import com.msa.client.S3Client;
import com.msa.config.Config;
import com.msa.interfaces.FileClient;

@Service
public class FileService {

  private final FileClient fileClient;

  public FileService() {
    this.fileClient = new AmazonS3FileClientAdapter(S3Client.getInstance());
  }

  public String uploadImage(MultipartFile img) {
    try {
      String imgFileNameParts[] = img.getOriginalFilename().split("\\.");
      String fileKey = UUID.randomUUID().toString() + "." + imgFileNameParts[imgFileNameParts.length - 1];
      return fileClient.uploadFile(Config.bucketName, fileKey, img.getInputStream(), img.getContentType(),
          img.getInputStream().available());
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
    return "";
  }

}
