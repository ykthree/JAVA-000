package learn.nio.gateway.outbound.netty4;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
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
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpVersion;
import org.jetbrains.annotations.NotNull;
import sun.nio.ch.Net;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 基于Netty的HttpClient
 */
public class NettyHttpClient {

    public void execute(String url, SimpleChannelInboundHandler<FullHttpResponse> handler) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            int port = uri.getPort();

            // 构建HTTP请求
            FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.getRawPath());
            request.headers().set(HttpHeaderNames.HOST, uri.getHost());
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            request.headers().set(HttpHeaderNames.ACCEPT_ENCODING, HttpHeaderValues.GZIP);

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
        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Shut down executor threads to exit.
            workerGroup.shutdownGracefully();
        }
    }
}