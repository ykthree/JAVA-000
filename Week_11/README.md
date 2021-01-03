# 作业说明

- Week_11
  - 作业 2（必做）：基于Redis封装分布式数据操作：
    1. 在 Java 中实现一个简单的分布式锁。

       - 代码片段
  
        分布式锁实现（RedisLock）：

        ```java

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

            private boolean isLocked() {
                ValueOperations<String, String> valueOperations = this.redisTemplate.opsForValue();
                return !valueOperations.setIfAbsent(DEFAULT_REDIS_LOCK, DEFAULT_REDIS_LOCK, DEFAULT_EXPIRE_SECONDS,
                        TimeUnit.SECONDS);
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
        ```

        获取锁 lua 脚本（lock.lua）

        ```
        if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then
            redis.call('expire', KEYS[1], ARGV[2]);
            return 1;
        else
            return 0;
        end
        ```

        释放锁 lua 脚本（release_lock.lua）

        ```
        if redis.call('get', KEYS[1]) == ARGV[1] then
            return redis.call('del', KEYS[1]);
        else
            return 0;
        end
        ```

        测试代码

        ```java
        @Test
        public void testLock() {
            lock.lock();
            //lock.lockWithLua();
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
            lock.lock();
            //lock.lockWithLua();
            log.info("main end");
        }
        ```

    2. 在 Java 中实现一个分布式计数器，模拟减库存。
   
       - 代码片段

        计数器实现（RedisCounter）
        ```java
        public class RedisCounter {

            private static final String REDIS_COUNTER_KEY = "counter";

            private StringRedisTemplate redisTemplate;

            public RedisCounter(int initialValue, String host, int port) {
                init(initialValue, host, port);
            }

            private void init(int initialValue, String host, int port) {
                RedisStandaloneConfiguration rsc = new RedisStandaloneConfiguration();
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
        ```
        减库存场景代码
        ```java
        public class InventoryService {

             /**
              * 商品库存缓存初始值
              */
             private static final int INVENTORY_AMOUNT = 100;

             private RedisCounter redisCounter;

             /**
              * 初始化 Redis 商品库存
              */
             @PostConstruct
             private void init() {
                 log.debug("初始化商品库存，库存数量：[{}]", INVENTORY_AMOUNT);
                 this.redisCounter = new RedisCounter(INVENTORY_AMOUNT, "127.0.0.1", 6379);
             }

             public String decreaseInventory() {
                int idleInventory = redisCounter.decreaseAndGet();
                log.debug("剩余库存数量：{}", idleInventory);
                if (idleInventory < 0) {
                    log.debug("No inventory!");
                    return "No inventory!";
                } else {
                    log.debug("success");
                    return "success";
                }
             }

        }

        @RestController
        @RequestMapping("/redis/usage/counter")
        public class CounterController {

            @Autowired
            private InventoryService inventoryService;

            @GetMapping("/flash")
            public String flashSale() {
                return inventoryService.decreaseInventory();
            }
            
        }

        ```
        使用 SuperBenchMarker 测试 
        ```
        sb -u http://localhost:8080/redis/usage/counter/flash -c 10 -n 110

        ```






# 学习笔记