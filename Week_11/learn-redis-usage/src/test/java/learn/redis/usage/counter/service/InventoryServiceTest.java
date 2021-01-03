package learn.redis.usage.counter.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 库存服务测试类
 *
 * @author ykthree
 * 2021/1/3
 */
@SpringBootTest
@Slf4j
public class InventoryServiceTest {

    @Autowired
    private InventoryService inventoryService;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Test
    public void testDecreaseInventory() {
        executorService.execute(() -> {
            String result = inventoryService.decreaseInventory();
            log.debug(result);
        });
    }

}
