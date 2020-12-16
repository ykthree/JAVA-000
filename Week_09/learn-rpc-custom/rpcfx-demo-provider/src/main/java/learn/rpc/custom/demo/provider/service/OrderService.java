package learn.rpc.custom.demo.provider.service;

import learn.rpc.custom.demo.api.domain.Order;
import learn.rpc.custom.demo.api.service.IOrderService;

public class OrderService implements IOrderService {

    @Override
    public Order findOrderById(int id) {
        return new Order(id, "CuiJing" + System.currentTimeMillis(), 9.9f);
    }

}
