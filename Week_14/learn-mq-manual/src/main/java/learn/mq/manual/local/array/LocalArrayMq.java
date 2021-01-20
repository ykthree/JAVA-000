package learn.mq.manual.local.array;

import learn.mq.manual.core.Message;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 内存 MQ，基于数组实现
 */
@Slf4j
class LocalArrayMq {

    /**
     * 队列消息容量
     */
    int size;

    /**
     * 记录下次读取元素的位置
     */
    int takeIndex;

    /**
     * 记录下次写入元素的位置
     */
    int putIndex;

    /**
     * 记录可读边界
     */
    int readBound;

    final Lock lock = new ReentrantLock();

    //final Condition notEmpty = lock.newCondition();

    //final Condition notFull = lock.newCondition();

    private Message[] queue;

    @Getter
    private String topic;

    private int capacity;

    LocalArrayMq(String topic, int capacity) {
        this.topic = Objects.requireNonNull(topic, "Topic's name must not be null!");
        if (capacity <= 0) {
            throw new RuntimeException("Capacity must greater than zero!");
        }
        this.capacity = capacity;
        queue = new Message[capacity];
    }

    boolean send(Message message) {
        lock.lock();
        try {
            // 队列已满
            if (size == capacity) {
                return false;
            }
            queue[putIndex++] = message;
            if (putIndex == capacity) {
                putIndex = 0;
            }
            size++;
            log.debug("After write queue: {}", Arrays.toString(queue));
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 生产者确认消息
     */
    boolean producerAck() {
        lock.lock();
        try {
            // 生产者确认后，可读边界后移
            readBound++;
        } finally {
            lock.unlock();
        }
        return true;
    }

    Message poll() {
        lock.lock();
        try {
            // 队列为空
            if (size == 0) {
                return null;
            }
            // 生产的消息未确认，不可读
            if (takeIndex == readBound) {
                return null;
            }
            Message message = queue[takeIndex];
            /*queue[takeIndex] = null;
            if (++ takeIndex == capacity) {
                takeIndex = 0;
            }
            size--;
            */
            log.debug("After read queue: {}", Arrays.toString(queue));
            return message;
        } finally {
            lock.unlock();
        }
    }

    Message poll(int offset) {
        lock.lock();
        try {
            // 队列为空
            if (size == 0) {
                return null;
            }
            // 生产的消息未确认，不可读
            if (offset == readBound) {
                return null;
            }
            Message message = queue[offset];
            /*queue[takeIndex] = null;
            if (++ takeIndex == capacity) {
                takeIndex = 0;
            }
            size--;
            */
            log.debug("After read queue: {}", Arrays.toString(queue));
            return message;
        } finally {
            lock.unlock();
        }
    }

    boolean consumerAck() {
        lock.lock();
        try {
            // 消费者确认后，删除确认消息，下次读取位置后移
            queue[takeIndex] = null;
            if (++takeIndex == capacity) {
                takeIndex = 0;
            }
            size--;
        } finally {
            lock.unlock();
        }
        return true;
    }

}
