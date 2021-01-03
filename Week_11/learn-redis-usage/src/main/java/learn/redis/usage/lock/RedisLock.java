package learn.redis.usage.lock;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 基于 Redis 实现的分布式锁
 *
 * @author ykthree
 * 2021/1/3
 */
public class RedisLock {

    /**
     * 默认锁的名字
     */
    private static final String DEFAULT_REDIS_LOCK = "lock";

    /**
     * 锁默认失效时间，单位秒
     */
    private static final int DEFAULT_EXPIRE_SECONDS = 10;

    private static final String LOCK_LUA_SCRIPT_PATH = "script/lock.lua";

    private static final String RELEASE_LOCK_LUA_SCRIPT_PATH = "script/release_lock.lua";

    private StringRedisTemplate redisTemplate;

    /**
     * 获取锁脚本
     */
    private DefaultRedisScript<Long> lockScript;

    /**
     * 释放锁脚本
     */
    private DefaultRedisScript<Long> releaseLockScript;

    public RedisLock(String host, int port) {
        init(host, port);
    }

    private void init(String host, int port) {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(host, port);
        lettuceConnectionFactory.afterPropertiesSet();
        this.redisTemplate = new StringRedisTemplate(lettuceConnectionFactory);
        this.redisTemplate.afterPropertiesSet();

        initLockScript();
        initReleaseLockScript();
    }

    private void initLockScript() {
        this.lockScript = new DefaultRedisScript<>();
        this.lockScript.setResultType(Long.class);
        this.lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(LOCK_LUA_SCRIPT_PATH)));
    }

    private void initReleaseLockScript() {
        this.releaseLockScript = new DefaultRedisScript<>();
        this.releaseLockScript.setResultType(Long.class);
        this.releaseLockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource(RELEASE_LOCK_LUA_SCRIPT_PATH)));
    }

    /**
     * 获取锁
     */
    public void lock() {
        boolean isLocked = isLocked();
        while (isLocked) {
            isLocked = isLocked();
        }
    }

    /**
     * 获取锁
     */
    public void lockWithLua() {
        boolean isLocked = isLockedWithLua();
        while (isLocked) {
            isLocked = isLockedWithLua();
        }
    }

    private boolean isLocked() {
        ValueOperations<String, String> valueOperations = this.redisTemplate.opsForValue();
        return !valueOperations.setIfAbsent(DEFAULT_REDIS_LOCK, DEFAULT_REDIS_LOCK, DEFAULT_EXPIRE_SECONDS,
                TimeUnit.SECONDS);
    }

    private boolean isLockedWithLua() {
        Long execute = this.redisTemplate.execute(lockScript, Collections.singletonList(DEFAULT_REDIS_LOCK),
                DEFAULT_REDIS_LOCK, String.valueOf(DEFAULT_EXPIRE_SECONDS));
        return 0 == execute.intValue();
    }

    /**
     * 释放锁
     */
    public void unlock() {
        this.redisTemplate.execute(releaseLockScript, Collections.singletonList(DEFAULT_REDIS_LOCK), DEFAULT_REDIS_LOCK);
    }

}
