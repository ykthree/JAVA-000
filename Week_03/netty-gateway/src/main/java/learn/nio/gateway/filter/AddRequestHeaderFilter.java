package learn.nio.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Objects;

/**
 * 添加Http请求头过滤器
 */
public class AddRequestHeaderFilter implements HttpRequestFilter {

    private final NameValueConfig nameValueConfig;

    public AddRequestHeaderFilter(NameValueConfig nameValueConfig) {
        Objects.requireNonNull(nameValueConfig, "NameValueConfig can not be null!");
        this.nameValueConfig = nameValueConfig;
    }

    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        fullRequest.headers().set(nameValueConfig.getName(), nameValueConfig.getValue());
    }

    public static class NameValueConfig {

        private final String name;

        private final Object value;

        public NameValueConfig(String name, Object value) {
            Objects.requireNonNull(name, "Name can not be null!");
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Object getValue() {
            return value;
        }
    }
}
