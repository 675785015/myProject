package com.xia.tec.mq.service;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;


/**
 * Created by Lee on 2018/3/27.
 */
@EnableJms
@Configuration
public class ActivemqConfig {

    @Bean
    public RedeliveryPolicy redeliveryPolicy(){
        RedeliveryPolicy  redeliveryPolicy=   new RedeliveryPolicy();
        //是否在每次尝试重新发送失败后,增长这个等待时间
        redeliveryPolicy.setUseExponentialBackOff(true);
        //重发次数,默认为6次   这里设置为10次
        redeliveryPolicy.setMaximumRedeliveries(10);
        //重发时间间隔,默认为1秒
        redeliveryPolicy.setInitialRedeliveryDelay(1);
        //第一次失败后重新发送之前等待500毫秒,第二次失败再等待500 * 2毫秒,这里的2就是value
        redeliveryPolicy.setBackOffMultiplier(2);
        //是否避免消息碰撞
        redeliveryPolicy.setUseCollisionAvoidance(false);
        //设置重发最大拖延时间-1 表示没有拖延只有UseExponentialBackOff(true)为true时生效
        redeliveryPolicy.setMaximumRedeliveryDelay(-1);
        return redeliveryPolicy;
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory (@Value("${spring.activemq.url}")String url,RedeliveryPolicy redeliveryPolicy){
        ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory(
                        "admin",
                        "admin",
                        url);
        activeMQConnectionFactory.setRedeliveryPolicy(redeliveryPolicy);
//        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
//        pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory);
        return activeMQConnectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate(ActiveMQConnectionFactory activeMQConnectionFactory) throws JMSException {

//        Connection connection = activeMQConnectionFactory.createConnection();
//        Session session = connection.createSession(Boolean.TRUE, Session.CLIENT_ACKNOWLEDGE);

        JmsTemplate jmsTemplate=new JmsTemplate();
        jmsTemplate.setDeliveryMode(2);//进行持久化配置 1表示非持久化，2表示持久化</span>
        jmsTemplate.setConnectionFactory(activeMQConnectionFactory);
        //jmsTemplate.setDefaultDestination(queue); //此处可不设置默认，在发送消息时也可设置队列
        jmsTemplate.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);//客户端签收模式
//        jmsTemplate.setSessionTransacted(Boolean.TRUE); //添加事务
        return jmsTemplate;
    }

    //定义一个消息监听器连接工厂，这里定义的是点对点模式的监听器连接工厂
    @Bean(name = "jmsQueueListener")
    public DefaultJmsListenerContainerFactory jmsQueueListenerContainerFactory(ActiveMQConnectionFactory activeMQConnectionFactory) {
        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(activeMQConnectionFactory);
        //设置连接数
        factory.setConcurrency("1-10");
        //重连间隔时间
        factory.setRecoveryInterval(10000L);
        factory.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
//        factory.setTransactionManager(new JmsTransactionManager());
        return factory;
    }
}
