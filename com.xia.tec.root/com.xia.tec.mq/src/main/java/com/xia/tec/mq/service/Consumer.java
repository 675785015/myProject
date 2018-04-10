package com.xia.tec.mq.service;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.TextMessage;

/**
 * Created by Lee on 2018/3/26.
 */
@Service
public class Consumer {

    @JmsListener(destination = "mytest.queue", containerFactory = "jmsQueueListener")
    public void receiveQueue(TextMessage text) throws JMSException {
        System.out.println("收到的信息是："+text.getText());
//        text.acknowledge();


    }

    @JmsListener(destination = "mytest.queue", containerFactory = "jmsQueueListener")
    public void receive(TextMessage text) throws JMSException {
        System.out.println("2收到的信息是："+text.getText());
//        text.acknowledge();


    }
}
