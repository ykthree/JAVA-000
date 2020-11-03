package learn.nio.gateway.outbound.netty4;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Get请求
 */
public class HttpGet implements HttpRequest {

    private final URI uri;

    private final List<HttpHeader> headers;

    HttpGet(Builder builder) {
        this.uri = builder.uri;
        this.headers = builder.headers;
    }

    @Override
    public String method() {
        return "GET";
    }

    @Override
    public URI uri() {
        return uri;
    }

    @Override
    public List<HttpHeader> headers() {
        return headers;
    }

    /**
     * Get请求构建器
     */
    public static class Builder {

        private URI uri;

        private final List<HttpHeader> headers = new ArrayList<>();

        public Builder uri(String url) {
            try {
                this.uri = new URI(url);
            } catch (URISyntaxException ignored) {}
            return this;
        }

        public Builder header(String key, String value) {
            this.headers.add(new HttpHeader(key, value));
            return this;
        }

        public Builder headers(List<HttpHeader> headers) {
            this.headers.addAll(headers);
            return this;
        }

        public HttpGet build() {
            Objects.requireNonNull(uri, "Uri is in wrong format or not set!");
            return new HttpGet(this);
        }
    }
}