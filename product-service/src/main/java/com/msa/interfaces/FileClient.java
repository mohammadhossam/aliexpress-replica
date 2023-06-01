package com.msa.interfaces;

import java.io.InputStream;

public interface FileClient {
  public String uploadFile(String bucketKey, String fileKey, InputStream inputStream, String contentType,
      long contentLength);
}
