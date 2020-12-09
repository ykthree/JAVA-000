package learn.spring.data.sharding;

import learn.spring.data.sharding.domain.entity.Order;
import learn.spring.data.sharding.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
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
            int insert = orderService.insert(buildOrder(i));
        }
        log.info("end");
    }

    @Test
    public void testSelectById() {
        for (int i = 1; i <= 16; i++) {
            Order order = orderService.selectById(i);
            assertNotNull(order);
            assertEquals(i, order.getId());
        }
        log.info("end");
    }

    @Test
    public void testListAllOrders() {
        List<Order> orders = orderService.listAllOrders();
        assertNotNull(orders);
        // TODO ... 查询时 0/1 会自动转换为 Boolean 类型
        assertEquals(16, orders.size());
    }

    @Test
    public void testCountAllOrders() {
        Integer integer = orderService.countAllOrders();
        assertEquals(16, integer);
    }

    @Test
    public void testDeleteById() {
        for (int i = 1; i <= 16; i++) {
            orderService.deleteById(i);
        }
        log.info("end");
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
