package com.example.mqtutorials.report;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class UserListener {

    @RabbitListener(queues = "user")
    public void receiveUser(String message) {
        System.out.println(" [x] user listener received: " + message);
    }
}
