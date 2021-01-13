package learn.mq.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Test
 *
 * @author ykthree
 * 2021/1/13 19:49
 */
@SpringBootTest
public class TopicServiceTest {

    @Autowired
    private TopicService topicService;

    @Test
    public void testSendMessage() {

        for (int i = 0; i < 100; i++) {
            topicService.sendMessage("message" + i);
        }
    }

}
