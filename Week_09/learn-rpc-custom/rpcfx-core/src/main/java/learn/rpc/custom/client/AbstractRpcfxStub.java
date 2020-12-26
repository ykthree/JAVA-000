package learn.rpc.custom.client;

import com.alibaba.fastjson.JSON;
import learn.rpc.custom.api.RpcfxRequest;
import learn.rpc.custom.api.RpcfxResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 客户端方法存根
 *
 * @author ykthree
 * 2020/12/25 21:14
 */
public abstract class AbstractRpcfxStub {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private static final OkHttpClient CLIENT = new OkHttpClient();

    private static final MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");

    /**
     * 创建存根
     *
     * @param serviceClass serviceClass
     * @param url          url
     * @param <T>          T
     * @return T
     * @throws Exception exception
     */
    public abstract <T> T create(final Class<T> serviceClass, final String url) throws Exception;

    protected RpcfxResponse doPost(RpcfxRequest req, String url) throws IOException {
        String reqJson = JSON.toJSONString(req);
        log.info("Request: {}", reqJson);
        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(reqJson, JSON_TYPE))
                .build();
        String respJson = CLIENT.newCall(request).execute().body().string();
        log.info("Response: {}", respJson);
        return JSON.parseObject(respJson, RpcfxResponse.class);
    }

}
