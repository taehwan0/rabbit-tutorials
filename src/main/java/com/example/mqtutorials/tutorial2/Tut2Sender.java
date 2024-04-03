package com.example.mqtutorials.tutorial2;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

//@Component
public class Tut2Sender {

    private final AtomicInteger dots = new AtomicInteger(0);
    private final AtomicInteger count = new AtomicInteger(0);

    @Autowired
    private RabbitTemplate template;
    @Autowired
    private Queue queue;

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    public void send() {
        StringBuilder builder = new StringBuilder("Hello");

        if (dots.incrementAndGet() == 4) {
            dots.set(1);
        }

        for (int i = 0; i < dots.get(); i++) {
            builder.append(',');
        }

        builder.append(count.incrementAndGet());

        String message = builder.toString();
        template.convertAndSend(queue.getName(), message);
        System.out.println(" [x] Sent '" + message + "'");
    }
}
