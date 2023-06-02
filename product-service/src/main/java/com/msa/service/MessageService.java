package com.msa.service;

import com.aliexpress.commonmodels.Message;
import org.springframework.stereotype.Service;

import com.msa.messagequeue.ProductServicePublisher;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {
  private final ProductServicePublisher publisher;

  public void publishMessage(Message message) {
    publisher.send(message);
  }
}
