package com.example.mqtutorials.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Value;

@Value
@JsonInclude(value = Include.NON_NULL)
public class Chat {

    String body;
    String userName;
}
