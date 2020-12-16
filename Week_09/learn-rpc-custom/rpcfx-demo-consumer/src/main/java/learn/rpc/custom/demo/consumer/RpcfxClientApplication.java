package learn.rpc.custom.demo.consumer;

import learn.rpc.custom.client.CglibRpcfxStub;
import learn.rpc.custom.client.RpcfxStub;
import learn.rpc.custom.demo.api.domain.Order;
import learn.rpc.custom.demo.api.domain.User;
import learn.rpc.custom.demo.api.service.IOrderService;
import learn.rpc.custom.demo.api.service.IUserService;

public class RpcfxClientApplication {

    private static final String SERVER =  "http://localhost:8080/";

    public static void main(String[] args) {
//        IUserService userService = RpcfxStub.create(IUserService.class, SERVER);
        IUserService userService = CglibRpcfxStub.create(IUserService.class, SERVER);
        User user = userService.findById(1);
        System.out.println("find user id=1 from server: " + user.getName());

//        IOrderService orderService = RpcfxStub.create(IOrderService.class, SERVER);
        IOrderService orderService = CglibRpcfxStub.create(IOrderService.class, SERVER);
        Order order = orderService.findOrderById(1992129);
        System.out.println(String.format("find order name=%s, amount=%f", order.getName(), order.getAmount()));
    }

}
