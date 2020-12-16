package learn.rpc.custom.demo.provider.config;

import learn.rpc.custom.demo.api.service.IOrderService;
import learn.rpc.custom.demo.api.service.IUserService;
import learn.rpc.custom.demo.provider.DemoServerResolver;
import learn.rpc.custom.demo.provider.service.OrderService;
import learn.rpc.custom.demo.provider.service.UserService;
import learn.rpc.custom.server.ReflectionInvoker;
import learn.rpc.custom.server.RpcfxResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RpcfxServerConfig {

    @Bean
    public RpcfxResolver demoServerResolver() {
        return new DemoServerResolver();
    }

    @Bean
    public ReflectionInvoker reflectionInvoker(RpcfxResolver demoServerResolver) {
        return new ReflectionInvoker(demoServerResolver);
    }

    @Bean(name = "learn.rpc.custom.demo.api.service.IUserService")
    public IUserService userService() {
        return new UserService();
    }

    @Bean(name = "learn.rpc.custom.demo.api.service.IOrderService")
    public IOrderService orderService() {
        return new OrderService();
    }

}
