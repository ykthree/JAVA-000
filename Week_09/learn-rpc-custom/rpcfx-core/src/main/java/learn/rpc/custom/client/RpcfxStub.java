package learn.rpc.custom.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import learn.rpc.custom.api.RpcfxRequest;
import learn.rpc.custom.api.RpcfxResponse;
import learn.rpc.custom.exception.RpcfxException;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 客户端代理存根，基于 JDK 动态代理实现
 */
public final class RpcfxStub {

    static {
        ParserConfig.getGlobalInstance().addAccept("learn.rpc");
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(final Class<T> serviceClass, final String url) {
        return (T) Proxy.newProxyInstance(RpcfxStub.class.getClassLoader(), new Class[]{serviceClass},
                new RpcfxInvocationHandler(serviceClass, url));
    }

    static class RpcfxInvocationHandler implements InvocationHandler {

        private static final OkHttpClient CLIENT = new OkHttpClient();

        private static final MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");

        private final Class<?> serviceClass;

        private final String url;

        public <T> RpcfxInvocationHandler(Class<T> serviceClass, String url) {
            this.serviceClass = serviceClass;
            this.url = url;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
            RpcfxRequest request = new RpcfxRequest();
            request.setServiceClass(this.serviceClass.getName());
            request.setMethod(method.getName());
            request.setParams(params);

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
