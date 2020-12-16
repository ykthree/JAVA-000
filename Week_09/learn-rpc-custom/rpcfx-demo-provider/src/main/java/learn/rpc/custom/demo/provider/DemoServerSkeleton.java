package learn.rpc.custom.demo.provider;

import learn.rpc.custom.api.RpcfxRequest;
import learn.rpc.custom.api.RpcfxResponse;
import learn.rpc.custom.server.ReflectionInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务端代理
 */
@RestController
public class DemoServerSkeleton {

    @Autowired
    private ReflectionInvoker rpcfxInvoker;

    @PostMapping("/")
    public RpcfxResponse invoke(@RequestBody RpcfxRequest request) {
        return rpcfxInvoker.invoke(request);
    }

}
