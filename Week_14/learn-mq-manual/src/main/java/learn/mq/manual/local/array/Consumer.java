package learn.mq.manual.local.array;

import learn.mq.manual.core.Message;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息消费者
 *
 * @param <T> 消息体格式
 */
public class Consumer<T> {

    private final Broker broker;

    private LocalArrayMq mq;

    // 记录偏移量
    private final ConcurrentHashMap<String, Integer> offsetMap = new ConcurrentHashMap<>();

    Consumer(Broker broker) {
        this.broker = broker;
    }

    public void subscribe(String topic) {
        this.mq = this.broker.findMq(topic);
        offsetMap.put(topic, 0);
        if (mq == null) throw new RuntimeException("Topic[" + topic + "] doesn't exist.");
    }

    public Message<T> receiveMessage() {
        return mq.poll();
    }

    public Message<T> receiveMessageAndAck() {
        Message message = mq.poll();
        if (message != null) {
            mq.consumerAck();
            recordOffset();
        }
        return message;
    }

    public Message<T> receiveMessageFromOffset() {
        Message message = mq.poll(offsetMap.get(mq.getTopic()));
        if (message != null) {
            mq.consumerAck();
            recordOffset();
        }
        return message;
    }

    private synchronized void recordOffset() {
        Integer integer = offsetMap.get(mq.getTopic());
        offsetMap.put(mq.getTopic(), ++integer);
    }

}
