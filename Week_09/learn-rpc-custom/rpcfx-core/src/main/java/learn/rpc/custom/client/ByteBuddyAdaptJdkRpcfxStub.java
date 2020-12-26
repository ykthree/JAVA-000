package learn.rpc.custom.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import learn.rpc.custom.api.RpcfxRequest;
import learn.rpc.custom.api.RpcfxResponse;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.matcher.ElementMatchers;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 客户端代理存根，基于 ByteBuddy 适配 JDK 动态代理实现
 */
public class ByteBuddyAdaptJdkRpcfxStub extends AbstractRpcfxStub {

    static {
        ParserConfig.getGlobalInstance().addAccept("learn.rpc");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T create(final Class<T> serviceClass, final String url) throws Exception {
        return (T) new ByteBuddy()
                .subclass(Object.class)
                .implement(serviceClass)
                .method(ElementMatchers.isDeclaredBy(serviceClass))
                .intercept(InvocationHandlerAdapter.of(new AccessServerInterceptor(serviceClass, url)))
                .make()
                .load(AbstractRpcfxStub.class.getClassLoader())
                .getLoaded()
                .newInstance();
    }

    public class AccessServerInterceptor implements InvocationHandler {

        private final Class<?> serviceClass;

        private final String url;

        public <T> AccessServerInterceptor(Class<T> serviceClass, String url) {
            this.serviceClass = serviceClass;
            this.url = url;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
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
