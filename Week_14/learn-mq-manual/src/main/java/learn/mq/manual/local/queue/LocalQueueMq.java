package learn.mq.manual.local.queue;

import learn.mq.manual.core.Message;
import lombok.SneakyThrows;

import java.util.Objects;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * 内存 MQ，基于 {@link BlockingDeque} 实现
 */
class LocalQueueMq {

    private BlockingDeque<Message> queue;

    private String topic;

    private int capacity;

    LocalQueueMq(String topic, int capacity) {
        this.topic = Objects.requireNonNull(topic, "Topic's name can not be null!");
        this.capacity = capacity;
        queue = new LinkedBlockingDeque<>(capacity);
    }

    boolean send(Message message) {
        return queue.offer(message);
    }

    Message poll() {
        queue.peek();
        return queue.poll();
    }

    @SneakyThrows
    Message poll(long timeout) {
        return queue.poll(timeout, TimeUnit.MILLISECONDS);
    }

}
