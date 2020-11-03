package learn.nio.gateway.inbound;

import com.sun.xml.internal.ws.api.server.HttpEndpoint;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import learn.nio.gateway.filter.AddRequestHeaderFilter;
import learn.nio.gateway.filter.HttpRequestFilter;
import learn.nio.gateway.outbound.HttpOutBoundHandler;
import learn.nio.gateway.outbound.httpclient4.HttpClientOutboundHandler;
import learn.nio.gateway.outbound.netty4.NettyHttpClientOutboundHandler;
import learn.nio.gateway.outbound.okhttp.OkHttpOutboundHandler;
import learn.nio.gateway.router.FullRandomEndpointRouter;
import learn.nio.gateway.router.HttpEndpointRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);

    private final List<String> proxyServers;

    private final HttpOutBoundHandler handler;

    private final HttpRequestFilter filter;

    private final HttpEndpointRouter router;
    
    public HttpInboundHandler(List<String> proxyServers) {
        this.proxyServers = proxyServers;
        filter = new AddRequestHeaderFilter(new AddRequestHeaderFilter.NameValueConfig("nio", "ykthree"));
        router = new FullRandomEndpointRouter();
        handler = new NettyHttpClientOutboundHandler(router.route(proxyServers));
//         handler = new HttpClientOutboundHandler(router.route(proxyServers));
//         handler = new OkHttpOutboundHandler(router.route(proxyServers));
    }
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            //logger.info("channelRead流量接口请求开始，时间为{}", startTime);
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
//            String uri = fullRequest.uri();
//            logger.info("接收到的请求url为{}", uri);
//            if (uri.contains("/test")) {
//                handlerTest(fullRequest, ctx);
//            }
            filter.filter(fullRequest, ctx);
            handler.handle(fullRequest, ctx);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

//    private void handlerTest(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
//        FullHttpResponse response = null;
//        try {
//            String value = "hello,kimmking";
//            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes("UTF-8")));
//            response.headers().set("Content-Type", "application/json");
//            response.headers().setInt("Content-Length", response.content().readableBytes());
//
//        } catch (Exception e) {
//            logger.error("处理测试接口出错", e);
//            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
//        } finally {
//            if (fullRequest != null) {
//                if (!HttpUtil.isKeepAlive(fullRequest)) {
//                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
//                } else {
//                    response.headers().set(CONNECTION, KEEP_ALIVE);
//                    ctx.write(response);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
//        ctx.close();
//    }

}
