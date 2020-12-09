package learn.spring.data.sharding.service.impl;

import learn.spring.data.sharding.domain.entity.Order;
import learn.spring.data.sharding.mapper.OrderMapper;
import learn.spring.data.sharding.service.IOrderService;
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
    public int insert(final Order order) {
        return mapper.insert(order);
    }

    @Override
    public Order selectById(final int id) {
        return mapper.selectById(id);
    }

    @Override
    public List<Order> listAllOrders() {
        return mapper.listAllOrders();
    }

    @Override
    public Integer countAllOrders() {
        return mapper.countAllOrders();
    }

    @Override
    public int deleteById(int id) {
        return mapper.deleteById(id);
    }

}
