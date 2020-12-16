package learn.rpc.custom.demo.api.service;

import learn.rpc.custom.demo.api.domain.Order;

public interface IOrderService {

    Order findOrderById(int id);

}
