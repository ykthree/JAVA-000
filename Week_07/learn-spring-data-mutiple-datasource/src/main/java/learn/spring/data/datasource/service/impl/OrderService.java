package learn.spring.data.datasource.service.impl;

import learn.spring.data.datasource.domain.entity.Order;
import learn.spring.data.datasource.mapper.OrderMapper;
import learn.spring.data.datasource.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单服务
 *
 * @author ykthree
 * 2020/11/29
 */
@Service
public class OrderService implements IOrderService {

    @Autowired
    private OrderMapper mapper;

    @Override
    public int[] batchInsert(final List<Order> orders) {
        return mapper.batchInsert(orders);
    }

    @Override
    public int insert(final Order order) {
        return mapper.insert(order);
    }

    @Override
    public Order selectById(int id) {
        return mapper.selectById(id);
    }

}
