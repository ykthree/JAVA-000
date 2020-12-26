package learn.rpc.custom.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import learn.rpc.custom.api.RpcfxRequest;
import learn.rpc.custom.api.RpcfxResponse;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.TargetMethodAnnotationDrivenBinder;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaConstant;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 客户端代理存根，基于 ByteBuddy 方法委托实现
 */
public final class ByteBuddyDelegationRpcfxStub extends AbstractRpcfxStub {

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
                .intercept(MethodDelegation.to(new AccessServerInterceptor(serviceClass, url)))
                .make()
                .load(serviceClass.getClassLoader())
                .getLoaded()
                .newInstance();
    }

    public class AccessServerInterceptor {

        private final Class<?> serviceClass;

        private final String url;

        public <T> AccessServerInterceptor(Class<T> serviceClass, String url) {
            this.serviceClass = serviceClass;
            this.url = url;
        }

        /**
         * {@link RuntimeType} 表示委托方法不确定原方法的具体返回类型，返回类型由运行时决定（即方法返回结果是动态的）。
         */
        @RuntimeType
        public Object interceptor(@Origin Method method, @AllArguments Object[] args) throws Exception {
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
