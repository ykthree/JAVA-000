package learn.mq.manual.local.array;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息服务，Broker + Connection
 */
public class Broker {

    public static final int CAPACITY = 10000;

    private final Map<String, LocalArrayMq> mqMap = new ConcurrentHashMap<>(64);

    public void createTopic(String name) {
        mqMap.putIfAbsent(name, new LocalArrayMq(name, CAPACITY));
    }

    public Producer createProducer() {
        return new Producer(this);
    }

    public Consumer createConsumer() {
        return new Consumer(this);
    }

    LocalArrayMq findMq(String topic) {
        return this.mqMap.get(topic);
    }

}
