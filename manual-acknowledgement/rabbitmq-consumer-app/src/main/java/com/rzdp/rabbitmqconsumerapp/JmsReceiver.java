package com.rzdp.rabbitmqconsumerapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

@Component
public class JmsReceiver implements ChannelAwareMessageListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        log.info("Fetching message content");
        byte[] body = message.getBody();

        String bodyStr = new String(body);

        ObjectMapper objectMapper = new ObjectMapper();
        Person person = objectMapper.readValue(bodyStr, Person.class);

        log.info("Person object successfully fetched. [{}]", person);
        log.info("Sending positive acknowledgement to queue");
        channel.basicAck(message.getMessageProperties()
                .getDeliveryTag(), false);
        log.info("Positive feedback sent");
    }
}