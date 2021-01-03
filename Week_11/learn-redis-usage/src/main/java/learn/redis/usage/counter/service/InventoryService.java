package learn.redis.usage.counter.service;

import learn.redis.usage.counter.RedisCounter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 操作商品库存
 *
 * @author ykthree
 * 2021/1/2
 */
@Service
@Slf4j
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
