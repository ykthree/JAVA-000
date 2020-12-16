package learn.rpc.custom.server;

import learn.rpc.custom.api.RpcfxRequest;
import learn.rpc.custom.api.RpcfxResponse;

/**
 * 服务端服务执行器
 */
public interface RpcfxInvoker {

    /**
     * 执行服务端服务
     *
     * @param rpcfxRequest
     * @return rpcfxResponse
     */
    RpcfxResponse invoke(RpcfxRequest request);

}
