package com.msa.service;

import org.springframework.stereotype.Service;

import com.msa.messagequeue.ProductServicePublisher;
import com.msa.model.Message;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {
  private final ProductServicePublisher publisher;

  public void publishMessage(Message message) {
    publisher.send(message);
  }
}
