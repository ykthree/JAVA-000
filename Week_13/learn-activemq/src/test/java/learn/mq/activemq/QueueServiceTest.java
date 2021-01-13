package learn.mq.activemq;

import learn.mq.activemq.service.QueueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QueueServiceTest {

	@Autowired
	private QueueService queueService;

	@Test
	public void testSendMessage() {
		for (int i = 0; i < 100; i++) {
			queueService.sendMessage( i + "message");
		}
	}

}
