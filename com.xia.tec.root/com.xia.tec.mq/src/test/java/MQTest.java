import com.xia.tec.mq.MQApplication;
import com.xia.tec.mq.service.Producer;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.Destination;

/**
 * Created by Lee on 2018/3/26.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MQApplication.class)
public class MQTest {

    @Autowired
    private Producer producer;

    @Test
    public void contextLoads() throws InterruptedException {
        Destination destination = new ActiveMQQueue("mytest.queue");
        for(int i=0; i<100; i++){
//                producer.sendMessage(destination, i+"--myname is chhliu!!!");
                producer.sendMessage2(destination, i+"--myname is xiaz!!!");
        }
    }
}
