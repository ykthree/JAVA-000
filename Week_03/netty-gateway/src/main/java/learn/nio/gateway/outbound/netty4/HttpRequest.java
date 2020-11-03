package learn.nio.gateway.outbound.netty4;

import java.net.URI;
import java.util.List;

/**
 * Http请求封装
 */
public interface HttpRequest {

    /**
     * Http请求类型
     *
     * @return Http请求类型
     */
    String method();

    /**
     * 获取请求URI
     *
     * @return URI
     */
    URI uri();

    /**
     * 获取{@link HttpHeader Http请求头}
     *
     * @return {@link HttpHeader Http请求头}
     */
    List<HttpHeader> headers();
}