package com.example.mqtutorials.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class TopicPathRequest {

    String username;
    String vhost;
    String resource;
    String name;
    String permission;
    @JsonProperty("routing_key")
    String routingKey;
}
