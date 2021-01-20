package learn.mq.manual.local.queue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息服务，Broker + Connection
 */
public class Broker {

    public static final int CAPACITY = 10000;

    private final Map<String, LocalQueueMq> mqMap = new ConcurrentHashMap<>(64);

    public void createTopic(String name) {
        mqMap.putIfAbsent(name, new LocalQueueMq(name, CAPACITY));
    }

    public Producer createProducer() {
        return new Producer(this);
    }

    public Consumer createConsumer() {
        return new Consumer(this);
    }

    LocalQueueMq findMq(String topic) {
        return this.mqMap.get(topic);
    }

}
