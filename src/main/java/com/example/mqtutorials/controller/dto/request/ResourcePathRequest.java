package com.example.mqtutorials.controller.dto.request;

import lombok.Value;

@Value
public class ResourcePathRequest {

    String username;
    String vhost;
    String resource;
    String name;
    String permission;
}
