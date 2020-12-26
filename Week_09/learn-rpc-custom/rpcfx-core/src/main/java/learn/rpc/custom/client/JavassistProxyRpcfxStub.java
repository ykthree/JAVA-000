package learn.rpc.custom.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;
import learn.rpc.custom.api.RpcfxRequest;
import learn.rpc.custom.api.RpcfxResponse;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 客户端代理存根，基于 Javassist 动态代理实现
 */
public final class JavassistProxyRpcfxStub extends AbstractRpcfxStub {

    static {
        ParserConfig.getGlobalInstance().addAccept("learn.rpc");
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T create(final Class<T> serviceClass, final String url) throws Exception {
        final ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(new Class[]{serviceClass});
        proxyFactory.setFilter(method -> !method.getName().equals("finalize"));
        T service = (T) proxyFactory.createClass().newInstance();
        Proxy proxy = (Proxy) service;
        proxy.setHandler(new AccessServerInterceptor(serviceClass, url));
        return service;
    }

    class AccessServerInterceptor implements MethodHandler {

        private final Class<?> serviceClass;

        private final String url;

        public <T> AccessServerInterceptor(Class<T> serviceClass, String url) {
            this.serviceClass = serviceClass;
            this.url = url;
        }

        @Override
        public Object invoke(Object o, Method method, Method proceed, Object[] args) throws Throwable {
            RpcfxRequest request = new RpcfxRequest();
            request.setServiceClass(this.serviceClass.getName());
            request.setMethod(method.getName());
            request.setParams(args);
            RpcfxResponse response = doPost(request, url);
            if (!response.isStatus()) {
                throw response.getException();
            }
            return JSON.parse(response.getResult().toString());
        }
    }

}
