package learn.rpc.custom.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import learn.rpc.custom.api.RpcfxRequest;
import learn.rpc.custom.api.RpcfxResponse;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 客户端代理存根，基于 JDK 动态代理实现
 */
public final class JdkRpcfxStub extends AbstractRpcfxStub {

    static {
        ParserConfig.getGlobalInstance().addAccept("learn.rpc");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T create(final Class<T> serviceClass, final String url) {
        return (T) Proxy.newProxyInstance(JdkRpcfxStub.class.getClassLoader(), new Class[]{serviceClass},
                new AccessSeverInterceptor(serviceClass, url));
    }

    class AccessSeverInterceptor implements InvocationHandler {

        private final Class<?> serviceClass;

        private final String url;

        public <T> AccessSeverInterceptor(Class<T> serviceClass, String url) {
            this.serviceClass = serviceClass;
            this.url = url;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
            RpcfxRequest request = new RpcfxRequest();
            request.setServiceClass(this.serviceClass.getName());
            request.setMethod(method.getName());
            request.setParams(params);
            RpcfxResponse response = doPost(request, this.url);
            if (!response.isStatus()) {
                throw response.getException();
            }
            return JSON.parse(response.getResult().toString());
        }
    }

}
