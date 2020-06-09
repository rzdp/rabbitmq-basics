package com.rzdp.rabbitmqpublisherapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitmqPublisherAppApplication implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public static void main(String[] args) {
        SpringApplication.run(RabbitmqPublisherAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Person person = new Person(1, "Juan Dela Cruz", 18);
        log.info("Sending Person Object to Queue: [{}]", person);
        rabbitTemplate.convertAndSend(
                RabbitmqConfig.TOPIC_EXCHANGE_NAME,
                RabbitmqConfig.ROUTING_KEY,
                person
        );
        log.info("Person object sent successfully to the queue!");
    }
}
