package learn.redis.usage.counter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * RedisCounterTest
 *
 * @author ykthree
 * 2021/1/3
 */
public class RedisCounterTest {

    private final String host = "127.0.0.1";

    private final int port = 6379;

    private RedisCounter redisCounter;

    @BeforeEach
    public void before() {
        this.redisCounter = new RedisCounter(10, host, port);
    }

    @Test
    public void testDecreaseAndGet() {
        int count = redisCounter.decreaseAndGet();
        Assertions.assertEquals(9, count);
    }

}
