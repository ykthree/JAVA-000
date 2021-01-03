package learn.redis.usage.counter;

import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis 计数器
 *
 * @author ykthree
 * 2021/1/3
 */
public class RedisCounter {

    /**
     * 计数器的名字
     */
    private static final String REDIS_COUNTER_KEY = "counter";

    private StringRedisTemplate redisTemplate;

    public RedisCounter(int initialValue, String host, int port) {
        init(initialValue, host, port);
    }

    private void init(int initialValue, String host, int port) {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(host, port);
        lettuceConnectionFactory.afterPropertiesSet();
        this.redisTemplate = new StringRedisTemplate(lettuceConnectionFactory);
        this.redisTemplate.afterPropertiesSet();
        this.redisTemplate.opsForValue().setIfAbsent(REDIS_COUNTER_KEY, String.valueOf(initialValue));
    }

    /**
     * 计数器减一，并返回减小后的值
     *
     * @return 减小后的值
     */
    public int decreaseAndGet() {
        return this.redisTemplate.opsForValue().decrement(REDIS_COUNTER_KEY).intValue();
    }

    /**
     * 计数器加一，并返回增加后的值
     *
     * @return 增加后的值
     */
    public int increaseAndGet() {
        return this.redisTemplate.opsForValue().increment(REDIS_COUNTER_KEY).intValue();
    }

    /**
     * 获取计数器当前的值
     *
     * @return 计数器当前的值
     */
    public int getCurrent() {
        return Integer.parseInt(this.redisTemplate.opsForValue().get(REDIS_COUNTER_KEY));
    }

    /**
     * 重置计数器
     *
     * @param initialValue 计数器初始值
     */
    public void reset(int initialValue) {
        this.redisTemplate.opsForValue().set(REDIS_COUNTER_KEY, String.valueOf(initialValue));
    }

}
