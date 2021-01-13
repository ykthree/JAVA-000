package learn.mq.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka
 *
 * @author ykthree
 * 2021/1/13 19:46
 */
@Service
public class TopicService {

    private final Logger logger = LoggerFactory.getLogger(TopicService.class);

    private static final String TOPIC_NAME = "test.topic";

    @Autowired
    private KafkaTemplate<Object, Object> template;

    public void sendMessage(String message) {
        this.template.send(TOPIC_NAME, message);
    }

    @KafkaListener(topics = TOPIC_NAME)
    public void receiveMessage(String message) {
        logger.info("Receive message : {}", message);
    }

}
