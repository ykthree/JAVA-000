package learn.rpc.custom.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import learn.rpc.custom.api.RpcfxRequest;
import learn.rpc.custom.api.RpcfxResponse;
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
import java.lang.reflect.Proxy;

/**
 * 客户端代理存根，基于 JDK 动态代理实现
 */
public final class CglibRpcfxStub {

    static {
        ParserConfig.getGlobalInstance().addAccept("learn.rpc");
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(final Class<T> serviceClass, final String url) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(serviceClass);
        enhancer.setCallback(new MethodInterceptorImpl(serviceClass, url));
        return (T) enhancer.create();
    }

    static class MethodInterceptorImpl implements MethodInterceptor {

        private static final OkHttpClient CLIENT = new OkHttpClient();

        private static final MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");

        private final Class<?> serviceClass;

        private final String url;

        public <T> MethodInterceptorImpl(Class<T> serviceClass, String url) {
            this.serviceClass = serviceClass;
            this.url = url;
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            RpcfxRequest request = new RpcfxRequest();
            request.setServiceClass(this.serviceClass.getName());
            request.setMethod(method.getName());
            request.setParams(args);

            RpcfxResponse response = post(request, url);
            if (!response.isStatus()) {
                throw response.getException();
            }
            return JSON.parse(response.getResult().toString());
        }

        private RpcfxResponse post(RpcfxRequest req, String url) throws IOException {
            String reqJson = JSON.toJSONString(req);
            System.out.println("req json: " + reqJson);
            final Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(reqJson, JSON_TYPE))
                    .build();
            String respJson = CLIENT.newCall(request).execute().body().string();
            System.out.println("resp json: " + respJson);
            return JSON.parseObject(respJson, RpcfxResponse.class);
        }

    }

}
