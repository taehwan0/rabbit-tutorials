package com.example.mqtutorials.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public RabbitAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public MessageConverter messageConverter() {
        ContentTypeDelegatingMessageConverter converter = new ContentTypeDelegatingMessageConverter(new Jackson2JsonMessageConverter());

        SimpleMessageConverter simpleMessageConverter = new SimpleMessageConverter();
        converter.addDelegate("text/plain", simpleMessageConverter);
        converter.addDelegate(null, simpleMessageConverter);

        return converter;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setPrefetchCount(3);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(3);

        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);

        return template;
    }

    @Bean
    public TopicExchange request() {
        return new TopicExchange("request");
    }

    @Bean
    public Queue command() {
        return new Queue("command");
    }

    @Bean
    public Binding binding(TopicExchange request, Queue command) {
        return BindingBuilder.bind(command).to(request).with("command.#");
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue("dead-letter");
    }

    @Bean
    public Queue buddyUserQueue() {
        return QueueBuilder.durable("user.buddy")
                       .deadLetterExchange("")
                       .deadLetterRoutingKey("dead-letter")
                       .build();
    }
}
