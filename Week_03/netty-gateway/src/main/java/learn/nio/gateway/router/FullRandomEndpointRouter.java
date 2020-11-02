package learn.nio.gateway.router;

import java.util.List;
import java.util.Random;

/**
 * 随机选取一个服务
 */
public class FullRandomEndpointRouter implements HttpEndpointRouter {

    private static final Random RANDOM = new Random();

    @Override
    public String route(List<String> endpoints) {
        int index = RANDOM.nextInt(endpoints.size());
        return endpoints.get(index);
    }
}
