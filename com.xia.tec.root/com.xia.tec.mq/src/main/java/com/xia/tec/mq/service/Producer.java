package com.xia.tec.mq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

/**
 * Created by Lee on 2018/3/26.
 */
@Service
public class Producer {
    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(Destination destination, final String messagge){
        jmsTemplate.convertAndSend(destination, messagge, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setIntProperty("order",1);
                return message;
            }
        });
    }

    public void sendMessage2(Destination destination, final String messagge){
        jmsTemplate.convertAndSend(destination, messagge, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setJMSCorrelationID("123456789");
                message.setIntProperty("order",2);
                return message;
            }
        });
    }
}
