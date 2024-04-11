package com.example.mqtutorials.listener;

import com.example.mqtutorials.domain.Chat;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Component;

@Slf4j
@RabbitListener(queues = "user.buddy")
@Component
public class ChatListener {

    @RabbitHandler
    public void receiveString(String input) {
        log.info(" [x] input message is instanceof: {}", input.getClass());
        log.info(" [x] 'String' input: {}", input);
    }

    @RabbitHandler
    public void receiveChat(
            Chat in,
            @Headers Map<String, Object> headers,
            @Header(name = AmqpHeaders.RECEIVED_ROUTING_KEY, required = false) String routingKey
    ) {
        System.out.println(" [x] Received Chat '" + in.getBody() + "' with routing key " + routingKey);
    }
}
