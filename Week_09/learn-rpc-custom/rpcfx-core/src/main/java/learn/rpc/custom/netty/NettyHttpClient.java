package learn.rpc.custom.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpVersion;

import java.net.URI;

/**
 * 基于Netty的HttpClient
 */
public class NettyHttpClient {

    public void execute(HttpRequest request, SimpleChannelInboundHandler<FullHttpResponse> handler) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            URI uri = request.uri();
            String host = uri.getHost();
            int port = uri.getPort();
            // 构建HTTP请求
            FullHttpRequest fullHttpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());
            HttpHeaders headers = fullHttpRequest.headers();
            headers.set(HttpHeaderNames.HOST, host)
                    .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE)
                    .set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);
            Bootstrap bootstrap = new Bootstrap()
                    .group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, false)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new HttpClientCodec());
                            p.addLast(new HttpObjectAggregator(1024 * 1024));
                            p.addLast(handler);
                        }
                    });
            // Start the client.
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            // Send the HTTP request.
            channelFuture.channel().writeAndFlush(request);
            // Wait for the server to close the connection.
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Shut down executor threads to exit.
            workerGroup.shutdownGracefully();
        }
    }
}