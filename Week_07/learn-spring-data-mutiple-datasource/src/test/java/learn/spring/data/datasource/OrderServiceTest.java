package learn.spring.data.datasource;

import learn.spring.data.datasource.domain.entity.Order;
import learn.spring.data.datasource.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * 订单服务测试类
 *
 * @author ykthree
 * 2020/11/29
 */
@SpringBootTest
@Slf4j
public class OrderServiceTest {

    @Autowired
    private IOrderService orderService;

    @Test
    public void testBatchInsert() {
        // 模拟批量插入 1000,000 订单数据
        long start = System.currentTimeMillis();
        for (int i = 1; i <= 1000; i++) {
            List<Order> orders = new ArrayList<>(1000);
            for (int j = 1; j <= 1000; j++) {
                orders.add(buildOrder());
            }
            // 批量插入，每次 1000 条
            int[] ints = orderService.batchInsert(orders);
            assertNotNull(ints);
            log.info("{}", Arrays.toString(ints));
        }
        // [1348613]ms 23 分钟
        log.info("订单生成结束，耗时：[{}]ms", System.currentTimeMillis() - start);
    }

    @Test
    public void testInsert() {
        int insert = orderService.insert(buildOrder());
        Order order = orderService.selectById(1);
        assertNotNull(order);
        assertEquals(1, order.getId());
    }

    @Test
    public void testSelectById() {
        for (int i = 0; i < 10; i++) {
            Order order = orderService.selectById(1);
            assertNotNull(order);
            assertEquals(1, order.getId());
        }
    }

    private Order buildOrder() {
        Order order = new Order();
        order.setOrderSn("AAA");
        order.setCustomerId(1);
        order.setOrderStatus((short) 1);
        order.setCreateTime(new Date());
        order.setPayTime(new Date());
        order.setShipTime(new Date());
        order.setReceiveTime(new Date());
        order.setDiscountMoney(new BigDecimal("30"));
        order.setShipMoney(new BigDecimal("0"));
        order.setPayMoney(new BigDecimal("99"));
        order.setPayMethod((short) 1);
        order.setAddress("CHANGSHA");
        order.setReceiveUser("Bob");
        order.setShipSn("BBB");
        order.setShipCompanyName("CCC");
        return order;
    }
}
