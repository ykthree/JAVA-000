package learn.mq.manual.local;

import learn.mq.manual.core.Message;
import learn.mq.manual.local.array.Broker;
import learn.mq.manual.local.array.Consumer;
import learn.mq.manual.local.array.Producer;
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
public class LocalArrayMqTests {

    private final String TOPIC_NAME = "test.topic";

    /**
     * 测试生产消息并全部确认
     * expect: 消息可全部被消费
     */
    @Test
    public void testSendAndAck() throws InterruptedException {
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
                Message message = consumer.receiveMessageAndAck();
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
            producer.sendMessageAndAck(TOPIC_NAME, stringMessage);
        }
        TimeUnit.MILLISECONDS.sleep(500);
        flag.set(false);
    }

    /**
     * 测试生产消息但全部不确认
     * expect: 消费不到消息
     */
    @Test
    public void testSendAndNoAck() throws InterruptedException {
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
                Message message = consumer.receiveMessageAndAck();
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
            producer.sendMessageAndAck(TOPIC_NAME, stringMessage);
        }
        TimeUnit.MILLISECONDS.sleep(500);
        flag.set(false);
    }

    /**
     * 测试生产消息但部分确认
     * expect: 只有被确认的消息才能被消费
     * TODO ... 需保证只有被确认的消息才能被消费
     */
    @Test
    public void testSendAndPartAck() throws InterruptedException {

    }

    /**
     * 测试生产消息并全部确认
     * expect: 消费全部被消息
     */
    @Test
    public void testPollAndAck() throws InterruptedException {
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
                Message message = consumer.receiveMessageAndAck();
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
            producer.sendMessageAndAck(TOPIC_NAME, stringMessage);
        }
        TimeUnit.MILLISECONDS.sleep(500);
        flag.set(false);
    }

    /**
     * 测试消费消息但全部不确认
     * expect: 一值消费同一条消息
     */
    @Test
    public void testPollAndNoAck() throws InterruptedException {
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
            producer.sendMessageAndAck(TOPIC_NAME, stringMessage);
        }
        TimeUnit.MILLISECONDS.sleep(500);
        flag.set(false);
    }

    @Test
    public void testPollByOffset() throws InterruptedException {
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
                Message message = consumer.receiveMessageFromOffset();
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
            producer.sendMessageAndAck(TOPIC_NAME, stringMessage);
        }
        TimeUnit.MILLISECONDS.sleep(500);
        flag.set(false);
    }


}
