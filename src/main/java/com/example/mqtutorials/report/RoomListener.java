package com.example.mqtutorials.report;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RoomListener {

    @RabbitListener(queues = "room")
    public void receiveUser(String message) {
        System.out.println(" [x] room listener received: " + message);
    }
}
