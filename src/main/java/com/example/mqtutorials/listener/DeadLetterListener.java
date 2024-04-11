package com.example.mqtutorials.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DeadLetterListener {

    @RabbitListener(queues = "dead-letter")
    public void receiveMessage(Message message) {
        log.info(" [x] Received DeadLetter\nHeader: {}\nBody: {}",
                message.getMessageProperties(),
                message.getBody());
    }
}
