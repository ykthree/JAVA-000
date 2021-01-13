package learn.mq.activemq;

import learn.mq.activemq.broker.EmbeddedMQServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import java.util.concurrent.TimeUnit;

/**
 * JmsTest
 *
 * @author ykthree
 * 2021/1/13 13:12
 */
@Slf4j
public class EmbeddedServerTest {

    private static final String BROKER_URL = "tcp://127.0.0.1:61616";

    private static final String QUEUE_NAME = "test.queue";

    private static final String TOPIC_NAME = "test.topic";

    private BrokerService brokerService;

    private ActiveMQConnection connection;

    private Session session;

    @BeforeEach
    public void createConnection() throws Exception {
        // 启动内嵌的 MQ Server
        //brokerService = EmbeddedMQServer.createServerExplicitly(BROKER_URL);
        //brokerService = EmbeddedMQServer.createServerByBrokerFactory(BROKER_URL);
        // brokerService.start();

        // 创建连接和会话
        // 同一个 JVM 进程里可以使用 vm://brokerName 连接
        // ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("vm://test");
        // ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        ActiveMQConnectionFactory factory = EmbeddedMQServer.createServerByActiveMQConnectionFactory();
        connection = (ActiveMQConnection) factory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();
    }

    @Test
    public void testQueue() throws JMSException, InterruptedException {
        Queue queue = session.createQueue(QUEUE_NAME);
        MessageProducer producer = session.createProducer(queue);
        // 创建消费者
        MessageConsumer consumer = session.createConsumer(queue);
        MessageListener listener = message -> {
            log.info("Message {} from destination {}.", message, QUEUE_NAME);
            // 前面所有未被确认的消息全部都确认
            // message.acknowledge();
        };
        // 绑定消息监听器
        consumer.setMessageListener(listener);
        for (int i = 0; i < 100; i++) {
            TextMessage message = session.createTextMessage(i + " message.");
            producer.send(message);
        }

        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    public void testTopic() throws JMSException, InterruptedException {
        Topic topic = session.createTopic(TOPIC_NAME);
        MessageProducer producer = session.createProducer(topic);
        // 创建消费者
        MessageConsumer consumer1 = session.createConsumer(topic);
        MessageConsumer consumer2 = session.createConsumer(topic);
        MessageListener listener = message -> {
            log.info("Message {} from destination {}.", message, QUEUE_NAME);
            // 前面所有未被确认的消息全部都确认
            // message.acknowledge();
        };
        // 绑定消息监听器
        consumer1.setMessageListener(listener);
        consumer2.setMessageListener(listener);

        for (int i = 0; i < 100; i++) {
            TextMessage message = session.createTextMessage(i + " message.");
            producer.send(message);
        }

        TimeUnit.SECONDS.sleep(2);
    }

    @AfterEach
    public void closeConnection() throws Exception {
        // 关闭连接
        if (connection != null) {
            connection.close();
        }
        if (session != null) {
            session.close();
        }
        if (brokerService != null) {
            brokerService.stop();
        }

    }

}
