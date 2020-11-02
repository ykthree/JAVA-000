package learn.nio.gateway.router;

import java.util.List;

public interface HttpEndpointRouter {

    /**
     * 从服务列表中选取一个服务作为服务提供方
     *
     * @param endpoints 服务列表
     * @return 服务端点
     */
    String route(List<String> endpoints);
}
