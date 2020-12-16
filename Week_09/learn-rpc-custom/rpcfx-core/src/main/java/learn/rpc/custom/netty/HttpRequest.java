package learn.rpc.custom.netty;

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

}