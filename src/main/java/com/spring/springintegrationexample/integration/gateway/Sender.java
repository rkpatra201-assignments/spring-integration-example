package com.spring.springintegrationexample.integration.gateway;

import org.springframework.integration.annotation.MessagingGateway;

import java.io.InputStream;

@MessagingGateway(defaultRequestChannel = "request.message.channel")
public interface Sender {
    public void send(InputStream message);
}