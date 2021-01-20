package learn.mq.manual.local;

import learn.mq.manual.core.Message;
import learn.mq.manual.local.queue.Broker;
import learn.mq.manual.local.queue.Consumer;
import learn.mq.manual.local.queue.Producer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 本地 MQ 测试类
 *
 * @author ykthree
 * 2021/1/18 16:38
 */
@Slf4j
public class LocalQueueMqTests {

    private final String TOPIC_NAME = "test.topic";

    @Test
    public void testLocalMq() throws InterruptedException {
        // 创建 Broker
        Broker broker = new Broker();
        // 创建 Topic
        broker.createTopic(TOPIC_NAME);
        // 创建消费者
        Consumer consumer = broker.createConsumer();
        consumer.subscribe(TOPIC_NAME);

        final AtomicBoolean flag = new AtomicBoolean(true);
        new Thread(() -> {
            while (flag.get()) {
                Message message = consumer.receiveMessage();
                if (message != null) {
                    log.info("Receive message [{}] from [{}].", message.getBody(), TOPIC_NAME);
                }
            }
            log.info("Exit ...");
        }).start();

        // 创建生产者
        Producer producer = broker.createProducer();
        // 发送消息
        for (int i = 0; i < 10; i++) {
            Message<String> stringMessage = new Message<>();
            stringMessage.setBody("Message<" + i + ">");
            producer.sendMessage(TOPIC_NAME, stringMessage);
        }
        TimeUnit.MILLISECONDS.sleep(500);
        flag.set(false);
    }

}
