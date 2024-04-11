package com.example.mqtutorials.controller.dto.request;

import lombok.Value;

@Value
public class UserPathRequest {

    String username;
    String vhost;
    String resource;
    String name;
    String permission;
}
