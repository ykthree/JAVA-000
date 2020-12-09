package learn.spring.data.sharding.service;

import learn.spring.data.sharding.domain.entity.Order;

import java.util.List;

/**
 * 订单服务
 *
 * @author ykthree
 * 2020/11/29
 */
public interface IOrderService {

    /**
     * 插入订单数据
     */
    int insert(final Order order);

    /**
     * 查询订单
     */
    Order selectById(final int id);

    /**
     * 查询全部订单
     *
     * @return 订单列表
     */
    List<Order> listAllOrders();

    /**
     * 查询全部订单数量
     *
     * @return 订单数量
     */
    Integer countAllOrders();

    /**
     * 删除订单
     */
    int deleteById(final int id);

}
