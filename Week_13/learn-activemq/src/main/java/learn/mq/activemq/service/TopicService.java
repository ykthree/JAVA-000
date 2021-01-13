package learn.mq.activemq.service;

import learn.mq.activemq.config.TopicsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Topic;

@Service
@Slf4j
public class TopicService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Topic testTopic;

    public void sendMessage(String message) {
        this.jmsTemplate.convertAndSend(this.testTopic, message);
    }

    // @JmsListener 需指定 containerFactory
    @JmsListener(destination = TopicsConfig.TOPIC_NAME/*, containerFactory = "jmsListenerContainerTopics"*/)
    public void receive1(String message) {
        log.info("Receiver1 receive message : {}", message);
    }

    @JmsListener(destination = TopicsConfig.TOPIC_NAME, containerFactory = "jmsListenerContainerTopics")
    public void receive2(String message) {
        log.info("Receiver2 receive message : {}", message);
    }

}