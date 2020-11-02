package learn.nio.gateway.outbound;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * HttpOutBoundHandler 处理接口定义
 */
public interface HttpOutBoundHandler {

    /**
     * 接收并处理Http请求
     *
     * @param fullRequest {@link FullHttpRequest}
     * @param ctx {@link ChannelHandlerContext}
     */
    void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx);
}
