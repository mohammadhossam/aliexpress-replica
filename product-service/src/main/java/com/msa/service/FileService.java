package com.msa.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.msa.client.S3Client;
import com.msa.config.Config;

@Service
public class FileService {

  private final AmazonS3 s3Client = S3Client.getInstance();

  public String uploadImage(MultipartFile img) {
    try {
      ObjectMetadata meta = new ObjectMetadata();
      meta.setContentLength(img.getInputStream().available());
      meta.setContentType(img.getContentType());
      String imgFileNameParts[] = img.getOriginalFilename().split("\\.");
      String key = UUID.randomUUID().toString() + "." + imgFileNameParts[imgFileNameParts.length - 1];
      PutObjectRequest request = new PutObjectRequest(Config.bucketName, key,
          img.getInputStream(), meta).withCannedAcl(CannedAccessControlList.PublicRead);
      s3Client.putObject(request);
      return s3Client.getUrl(Config.bucketName, key).toString();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return "FAILED_UPLOAD";
  }

}
