package com.xia.tec.mq.outspring;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Created by Lee on 2018/3/28.
 */
public class SimpleConsumer {
    /** JNDI name for ConnectionFactory */
    static final String CONNECTION_FACTORY_JNDI_NAME = "ConnectionFactory";
    /** JNDI name for Queue Destination (use for PTP Mode) */
    static final String QUEUE_JNDI_NAME = "exampleQueue";
    /** JNDI name for Topic Destination (use for Pub/Sub Mode) */
    static final String TOPIC_JNDI_NAME = "exampleTopic";
    /**
     * @param args
     */
    public static void main(String[] args) {
        Context jndiContext = null;
        ConnectionFactory connectionFactory = null;
        Connection connection = null;
        Session session = null;
//        Destination destination = null;
        Topic topic = null;
        MessageConsumer consumer = null;
        // create a JNDI API IntialContext object
        try {
            jndiContext = new InitialContext();
            connectionFactory = (ConnectionFactory) jndiContext
                    .lookup(CONNECTION_FACTORY_JNDI_NAME);
            // look up QUEUE_JNDI_NAME for PTP Mode
            // look up TOPIC_JNDI_NAME for Pub/Sub Mode
            topic = (Topic) jndiContext.lookup(TOPIC_JNDI_NAME);
        } catch (NamingException e) {
            System.out.println("Could not create JNDI Context:"
                    + e.getMessage());
            System.exit(1);
        }
        // receive Messages and finally release the resources.
        try {
            connection = connectionFactory.createConnection();
            connection.setClientID("Client-20160406"); // 为Connection指定ClientID
            connection.start();
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            consumer = session.createDurableSubscriber(topic, "subscriber-2016040601"); // 为subscriber指定name
//            consumer = session.createConsumer(topic);

//            long timeout = 10*1000;
//            for (Message message = consumer.receive(timeout); message != null; message = consumer
//                    .receive(timeout)) {
//            Message receive = consumer.receive();
//            String text = ((TextMessage) receive).getText();
//                System.out.println(String.format("receive a message:%s", text));
//            }
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    String text = null;
                    try {
                        text = ((TextMessage) message).getText();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                    System.out.println(String.format("receive a message:%s", text));
                }
            });
//            session.commit();
        } catch (JMSException e) {
            e.printStackTrace();
//            try {
//                session.rollback();
//            } catch (JMSException e1) {
//                e1.printStackTrace();
//            }
        }finally {
                try {
                    if(session!=null) {
                        session.close();
                    }
                    if(connection!=null){
                        connection.close();
                    }
                } catch (JMSException e) {
                    e.printStackTrace();
                }
        }
        }
    }
