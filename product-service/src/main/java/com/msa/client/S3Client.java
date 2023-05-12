package com.msa.client;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.msa.config.Config;

public class S3Client {

  private static AmazonS3 instance;

  private S3Client() {
  }

  public static AmazonS3 getInstance() {
    if (instance == null) {
      instance = AmazonS3ClientBuilder
          .standard()
          .withCredentials(new AWSStaticCredentialsProvider(Config.awsCredentials))
          .withRegion(Regions.EU_CENTRAL_1)
          .build();
    }
    return instance;
  }

}
