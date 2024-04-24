package com.example.mqtutorials.report;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReportConfiguration {

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topic");
    }

    @Bean
    public TopicExchange chatExchange() {
        return new TopicExchange("chat");
    }

    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange("user");
    }

    @Bean
    public TopicExchange roomExchange() {
        return new TopicExchange("room");
    }

    @Bean
    public Queue commandQueue() {
        return new Queue("command");
    }

    @Bean
    public Queue userQueue() {
        return new Queue("user");
    }

    @Bean
    public Queue roomQueue() {
        return new Queue("room");
    }

    @Bean
    public Binding bindingChatExchange(TopicExchange topicExchange, TopicExchange chatExchange) {
        return BindingBuilder.bind(topicExchange).to(chatExchange).with("chat.#");
    }

    @Bean
    public Binding bindingUserExchange(TopicExchange chatExchange, TopicExchange userExchange) {
        return BindingBuilder.bind(chatExchange).to(userExchange).with("*.user.#");
    }

    @Bean
    public Binding bindingRoomExchange(TopicExchange chatExchange, TopicExchange roomExchange) {
        return BindingBuilder.bind(chatExchange).to(roomExchange).with("*.room.#");
    }

    @Bean
    public Binding bindingCommandQueue(Queue commandQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(commandQueue).to(topicExchange).with("command.#");
    }

    @Bean
    public Binding bindingChatUserQueue(Queue userQueue, TopicExchange userExchange) {
        return BindingBuilder.bind(userQueue).to(userExchange).with("*.user.#");
    }

    @Bean
    public Binding bindingChatRoomQueue(Queue roomQueue, TopicExchange roomExchange) {
        return BindingBuilder.bind(roomQueue).to(roomExchange).with("*.room.#");
    }
}
