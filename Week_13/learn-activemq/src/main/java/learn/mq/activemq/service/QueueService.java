package learn.mq.activemq.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;

/**
 * Queue
 *
 * @author ykthree
 * 2021/1/13 00:29
 */
@Slf4j
@Service
public class QueueService {

    private final static String QUEUE_NAME = "test.queue";

    @Autowired
    private JmsTemplate jmsTemplate;

    public String sendMessage(String queuesMsg) {
        jmsTemplate.send(QUEUE_NAME, session -> {
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText(queuesMsg);
            return textMessage;
        });
        return queuesMsg;
    }

    /**
     * 消息消费者(点对点)
     */
    @JmsListener(destination = QUEUE_NAME)
    public void receiveMessage(String message) {
        log.info("Message {} from queue {}.", message, QUEUE_NAME);
    }

}
