package learn.rpc.custom.netty;

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

    HttpGet(Builder builder) {
        this.uri = builder.uri;
    }

    @Override
    public String method() {
        return "GET";
    }

    @Override
    public URI uri() {
        return uri;
    }


    /**
     * Get请求构建器
     */
    public static class Builder {

        private URI uri;

        public Builder uri(String url) {
            try {
                this.uri = new URI(url);
            } catch (URISyntaxException ignored) {}
            return this;
        }

        public HttpGet build() {
            Objects.requireNonNull(uri, "Uri is in wrong format or not set!");
            return new HttpGet(this);
        }
    }
}