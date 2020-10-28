package learn.nio.client;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Http客户端
 *
 * @author ykthree
 * @date 2020/10/27 22:40
 */
public class HttpClient {

    private static final Logger log = LoggerFactory.getLogger(HttpClient.class);

    private static final OkHttpClient CLIENT = new OkHttpClient();

    private static final String BASE_URL = "http://localhost:8801";

    /**
     * 发起http请求，返回字符串
     *
     * @param url 请求url
     * @return 响应字符串
     * @throws IOException 请求失败
     */
    public static String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + url)
                .build();
        try (Response response = CLIENT.newCall(request).execute()) {
            ResponseBody body = response.body();
            if (body != null) {
                return body.string();
            }
            return "";
        }
    }

    public static void main(String[] args) throws IOException {
        String response = run("/");
        log.info("Response:[{}]", response);
    }
}
