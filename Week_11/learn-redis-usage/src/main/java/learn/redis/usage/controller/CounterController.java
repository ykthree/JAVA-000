package learn.redis.usage.controller;

import learn.redis.usage.counter.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 计数器场景
 *
 * @author ykthree
 * 2021/1/2
 */
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
