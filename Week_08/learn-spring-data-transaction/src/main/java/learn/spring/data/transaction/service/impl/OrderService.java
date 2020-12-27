package learn.spring.data.transaction.service.impl;

import learn.spring.data.transaction.domain.entity.Order;
import learn.spring.data.transaction.exception.RollbackException;
import learn.spring.data.transaction.mapper.OrderMapper;
import learn.spring.data.transaction.service.IOrderService;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = RollbackException.class)
    @ShardingTransactionType(TransactionType.XA)
    public TransactionType insert(final Order order) {
        mapper.insert(order);
        return TransactionTypeHolder.get();
    }

    @Override
    @Transactional(rollbackFor = RollbackException.class)
    @ShardingTransactionType(TransactionType.XA)
    public void insertThenRollback(final List<Order> orders) throws RollbackException {
        mapper.batchInsert(orders);
        throw new RollbackException("Mock access failed");
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
