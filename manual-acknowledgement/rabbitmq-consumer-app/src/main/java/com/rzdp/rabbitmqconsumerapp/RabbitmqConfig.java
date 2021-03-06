package com.rzdp.rabbitmqconsumerapp;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    public static final String TOPIC_EXCHANGE_NAME = "MESSAGE_QUEUE_EXCHANGE";
    public static final String QUEUE_NAME = "MESSAGE_QUEUE";
    public static final String ROUTING_KEY = "MESSAGE_ROUTING_KEY";

    private static final String CONNECTION_FACTORY_HOST = "localhost";
    private static final int CONNECTION_FACTORY_PORT = 5672;
    private static final String CONNECTION_FACTORY_USERNAME = "guest";
    private static final String CONNECTION_FACTORY_PASSWORD = "guest";

    @Autowired
    private JmsReceiver jmsReceiver;

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(topicExchange())
                .with(ROUTING_KEY);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(CONNECTION_FACTORY_HOST);
        connectionFactory.setPort(CONNECTION_FACTORY_PORT);
        connectionFactory.setUsername(CONNECTION_FACTORY_USERNAME);
        connectionFactory.setUsername(CONNECTION_FACTORY_PASSWORD);
        return connectionFactory;
    }

    @Bean
    public SimpleMessageListenerContainer container() {
        SimpleMessageListenerContainer container =
                new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter());
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter() {
        return new MessageListenerAdapter(jmsReceiver, "receiveMessage");
    }
}
