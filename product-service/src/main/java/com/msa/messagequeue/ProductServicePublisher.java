package com.msa.messagequeue;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductServicePublisher {
  private final RabbitTemplate rabbitTemplate;
  private final Queue queue;

  public void send(String message) {
    rabbitTemplate.convertAndSend(queue.getName(), message);
  }
}
