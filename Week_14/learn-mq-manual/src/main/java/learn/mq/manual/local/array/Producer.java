package learn.mq.manual.local.array;

import learn.mq.manual.core.Message;

/**
 * 消息生成者
 */
public class Producer {

    private final Broker broker;

    Producer(Broker broker) {
        this.broker = broker;
    }

    /**
     * 发送消息
     *
     * @param topic   消息发送目的地
     * @param message 消息
     * @return 消息是否发送成功
     */
    public boolean sendMessage(String topic, Message message) {
        LocalArrayMq mq = this.broker.findMq(topic);
        if (null == mq) throw new RuntimeException("Topic[" + topic + "] doesn't exist.");
        return mq.send(message);
    }

    /**
     * 发送消息并确认
     *
     * @param topic   消息发送目的地
     * @param message 消息
     * @return 消息是否发送成功
     */
    public boolean sendMessageAndAck(String topic, Message message) {
        LocalArrayMq mq = this.broker.findMq(topic);
        if (null == mq) throw new RuntimeException("Topic[" + topic + "] doesn't exist.");
        boolean send = mq.send(message);
        if (send) {
            mq.producerAck();
        }
        return send;
    }

}
