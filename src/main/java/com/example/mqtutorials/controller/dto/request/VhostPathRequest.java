package com.example.mqtutorials.controller.dto.request;

import lombok.Value;

@Value
public class VhostPathRequest {

    String username;
    String vhost;
    String ip;
}
