package learn.nio.gateway.inbound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import learn.nio.gateway.outbound.httpclient4.HttpClientOutboundHandler;
import learn.nio.gateway.outbound.netty4.NettyHttpClientOutboundHandler;

import java.util.List;

public class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {
	
	private final List<String> proxyServers;
	
	public HttpInboundInitializer(List<String> proxyServers) {
		this.proxyServers = proxyServers;
	}
	
	@Override
	public void initChannel(SocketChannel ch) {
		ChannelPipeline p = ch.pipeline();
//		if (sslCtx != null) {
//			p.addLast(sslCtx.newHandler(ch.alloc()));
//		}
		p.addLast(new HttpServerCodec());
		//p.addLast(new HttpServerExpectContinueHandler());
		p.addLast(new HttpObjectAggregator(1024 * 1024));
		p.addLast(new HttpInboundHandler(this.proxyServers));
	}
}
