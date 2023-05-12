package com.msa.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;

public class Config {

  public static AWSCredentials awsCredentials = new BasicAWSCredentials("${PRODUCT_SERVICE_AWS_ACCESS_KEY}",
      "${PRODUCT_SERVICE_AWS_SECRET_KEY}");

  public static String bucketName = "aliexpress-replica-product-images";
}
