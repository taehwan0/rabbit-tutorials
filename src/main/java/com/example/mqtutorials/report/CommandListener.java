package com.example.mqtutorials.report;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CommandListener {

    @RabbitListener(queues = "command")
    public void receiveUser(String message) {
        System.out.println(" [x] command listener received: " + message);
    }
}
