package learn.mq.manual.local.queue;

import learn.mq.manual.core.Message;

/**
 * 消息消费者
 *
 * @param <T> 消息体格式
 */
public class Consumer<T> {

    private final Broker broker;

    private LocalQueueMq mq;

    Consumer(Broker broker) {
        this.broker = broker;
    }

    public void subscribe(String topic) {
        this.mq = this.broker.findMq(topic);
        if (mq == null) throw new RuntimeException("Topic[" + topic + "] doesn't exist.");
    }

    public Message<T> receiveMessage() {
        return mq.poll();
    }

}
