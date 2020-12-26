package learn.rpc.custom.demo.consumer;

import learn.rpc.custom.client.AbstractRpcfxStub;
import learn.rpc.custom.client.ByteBuddyAdaptJdkRpcfxStub;
import learn.rpc.custom.client.ByteBuddyDelegationRpcfxStub;
import learn.rpc.custom.client.CglibRpcfxStub;
import learn.rpc.custom.client.JavassistProxyRpcfxStub;
import learn.rpc.custom.client.JavassistRpcfxStub;
import learn.rpc.custom.client.JdkRpcfxStub;
import learn.rpc.custom.demo.api.domain.Order;
import learn.rpc.custom.demo.api.domain.User;
import learn.rpc.custom.demo.api.service.IOrderService;
import learn.rpc.custom.demo.api.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcfxClientApplication {

    private static final Logger log = LoggerFactory.getLogger(RpcfxClientApplication.class);

    private static final String SERVER = "http://localhost:8080/";

    public static void main(String[] args) throws Exception {
//        accessWithJdkStub();
//        accessWithCglibStub();
        accessWithJavassistStub();
//        accessWithJavassistProxyStub();
//        accessWithByteBuddyAdaptJdkStub();
//        accessWithByteBuddyDelegationStub();

    }

    /**
     * 使用基于 JDK 动态代理实现的客户端存根
     */
    private static void accessWithJdkStub() throws Exception {
        log.info("Access server by JdkStub");
        AbstractRpcfxStub stub = new JdkRpcfxStub();

        IUserService userService = stub.create(IUserService.class, SERVER);
        User user = userService.findById(1);
        log.info("Find user id=1 from server: {}", user.getName());

        IOrderService orderService = stub.create(IOrderService.class, SERVER);
        Order order = orderService.findOrderById(1992129);
        log.info("Find order name={}, amount={}", order.getName(), order.getAmount());

    }

    /**
     * 使用基于 Cglib 动态代理实现的客户端存根
     */
    private static void accessWithCglibStub() throws Exception {
        log.info("Access server by JdkStub");
        AbstractRpcfxStub stub = new CglibRpcfxStub();

        IUserService userService = stub.create(IUserService.class, SERVER);
        User user = userService.findById(1);
        log.info("Find user id=1 from server: {}", user.getName());

        IOrderService orderService = stub.create(IOrderService.class, SERVER);
        Order order = orderService.findOrderById(1992129);
        log.info("Find order name={}, amount={}", order.getName(), order.getAmount());
    }

    /**
     * 使用基于 Javassist 实现的客户端存根
     */
    private static void accessWithJavassistStub() throws Exception {
        log.info("Access server by JavassistRpcfxStub");
        AbstractRpcfxStub stub = new JavassistRpcfxStub();

        IUserService userService = stub.create(IUserService.class, SERVER);
        User user = userService.findById(1);
        log.info("Find user id=1 from server: {}", user.getName());

        IOrderService orderService = stub.create(IOrderService.class, SERVER);
        Order order = orderService.findOrderById(1992129);
        log.info("Find order name={}, amount={}", order.getName(), order.getAmount());
    }

    /**
     * 使用基于 Javassist 代理实现的客户端存根
     */
    private static void accessWithJavassistProxyStub() throws Exception {
        log.info("Access server by JavassistProxyRpcfxStub");
        AbstractRpcfxStub stub = new JavassistProxyRpcfxStub();

        IUserService userService = stub.create(IUserService.class, SERVER);
        User user = userService.findById(1);
        log.info("Find user id=1 from server: {}", user.getName());

        IOrderService orderService = stub.create(IOrderService.class, SERVER);
        Order order = orderService.findOrderById(1992129);
        log.info("Find order name={}, amount={}", order.getName(), order.getAmount());

    }

    /**
     * 使用基于 ByteBuddy 方法委托实现的客户端存根
     */
    private static void accessWithByteBuddyDelegationStub() throws Exception {
        log.info("Access server by ByteBuddyDelegationStub");
        AbstractRpcfxStub stub = new ByteBuddyDelegationRpcfxStub();

        IUserService userService = stub.create(IUserService.class, SERVER);
        User user = userService.findById(1);
        log.info("Find user id=1 from server: {}", user.getName());

        IOrderService orderService = stub.create(IOrderService.class, SERVER);
        Order order = orderService.findOrderById(1992129);
        log.info("Find order name={}, amount={}", order.getName(), order.getAmount());
    }

    /**
     * 使用基于 ByteBuddy 适配 JDK 动态代理实现的客户端存根
     */
    private static void accessWithByteBuddyAdaptJdkStub() throws Exception {
        log.info("Access server by ByteBuddyAdaptJdkRpcfxStub");
        AbstractRpcfxStub stub = new ByteBuddyAdaptJdkRpcfxStub();

        IUserService userService = stub.create(IUserService.class, SERVER);
        User user = userService.findById(1);
        log.info("Find user id=1 from server: {}", user.getName());

        IOrderService orderService = stub.create(IOrderService.class, SERVER);
        Order order = orderService.findOrderById(1992129);
        log.info("Find order name={}, amount={}", order.getName(), order.getAmount());
    }

}
