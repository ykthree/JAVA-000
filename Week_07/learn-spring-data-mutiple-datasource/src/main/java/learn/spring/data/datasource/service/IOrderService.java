package learn.spring.data.datasource.service;

import learn.spring.data.datasource.domain.entity.Order;

import java.util.List;

/**
 * 订单服务
 *
 * @author ykthree
 * 2020/11/29
 */
public interface IOrderService {

    /**
     * 批量插入订单数据
     */
    int[] batchInsert(final List<Order> orders);

    /**
     * 插入订单数据
     */
    int insert(final Order order);

    /**
     * 查询订单
     */
    Order selectById(int id);

}
