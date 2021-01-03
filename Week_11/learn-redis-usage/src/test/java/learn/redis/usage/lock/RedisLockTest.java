package learn.redis.usage.lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * RedisLockTest
 *
 * @author ykthree
 * 2021/1/3
 */
@Slf4j
@SpringBootTest
public class RedisLockTest {

    private final RedisLock lock = new RedisLock("127.0.0.1", 6379);

    @Test
    public void testLock() {
//        lock.lock();
        lock.lockWithLua();
        Thread thread = new Thread(() -> {
            try {
                log.info("start");
                TimeUnit.SECONDS.sleep(5);
                log.info("end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });
        thread.start();
//        lock.lock();
        lock.lockWithLua();
        log.info("main end");
    }

}
