package com.msa.adapters;

import java.io.InputStream;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.msa.interfaces.FileClient;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor()
public class AmazonS3FileClientAdapter implements FileClient {

  private final AmazonS3 s3Client;

  @Override
  public String uploadFile(String bucketKey, String fileKey, InputStream inputStream, String contentType,
      long contentLength) {
    ObjectMetadata objectMetaData = new ObjectMetadata();
    objectMetaData.setContentType(contentType);
    objectMetaData.setContentLength(contentLength);
    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketKey, fileKey, inputStream, objectMetaData);
    putObjectRequest = putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
    s3Client.putObject(putObjectRequest);
    return s3Client.getUrl(bucketKey, fileKey).toString();
  }

}
