package com.xia.tec.mq.outspring;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Created by Lee on 2018/3/28.
 */
public class SimpleProducer {

    /**
     * JNDI name for ConnectionFactory
     */
    static final String CONNECTION_FACTORY_JNDI_NAME = "ConnectionFactory";
    /**
     * JNDI name for Queue Destination (use for PTP Mode)
     */
    static final String QUEUE_JNDI_NAME = "exampleQueue";
    /**
     * JNDI name for Topic Destination (use for Pub/Sub Mode)
     */
    static final String TOPIC_JNDI_NAME = "exampleTopic";

    public static void main(String[] args) {
        Context jndiContext = null;
        ConnectionFactory connectionFactory = null;
        Connection connection = null;
        Session session = null;
//        Destination destination = null;
        Topic topic = null;
        MessageProducer messageProducer = null;

        try {
            jndiContext = new InitialContext();
            connectionFactory = (ConnectionFactory) jndiContext.lookup(CONNECTION_FACTORY_JNDI_NAME);
            topic = (Topic) jndiContext.lookup(TOPIC_JNDI_NAME);
            connection = connectionFactory.createConnection();
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);//1事务  2签收方式
            TextMessage textMessage = session.createTextMessage();
            messageProducer = session.createProducer(topic);
            for (int i = 0; i < 3; i++) {
                textMessage.setText(String.format("This is the %dth message.",
                        i + 1));
                messageProducer.send(textMessage);
            }
//            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
//            try {
//                session.rollback();
//            } catch (JMSException e1) {
//                e1.printStackTrace();
//            }
        } finally {

            try {
                if (session != null) {
                    session.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }


}
