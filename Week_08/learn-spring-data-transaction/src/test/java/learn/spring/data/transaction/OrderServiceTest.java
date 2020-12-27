package learn.spring.data.transaction;

import learn.spring.data.transaction.domain.entity.Order;
import learn.spring.data.transaction.exception.RollbackException;
import learn.spring.data.transaction.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    public void testInsert() {
        for (int i = 1; i <= 16; i++) {
            TransactionType transactionType = orderService.insert(buildOrder(i));
            orderService.deleteById(i);
            log.info("TransactionType: [{}]", transactionType); // XA
        }
        log.info("end");
    }

    @Test
    public void testInsertThenRollback() {
        Order order1 = buildOrder(1);
        Order order2 = buildOrder(2);
        try {
            orderService.insertThenRollback(Arrays.asList(order1, order2));
        } catch (RollbackException e) {
            log.error("failed", e);
        }
        Integer integer = orderService.countAllOrders();
        assertEquals(0, integer);
    }

    private Order buildOrder(int id) {
        Order order = new Order();
        order.setId(id);
        order.setOrderSn("AAA");
        order.setCustomerId(id);
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
