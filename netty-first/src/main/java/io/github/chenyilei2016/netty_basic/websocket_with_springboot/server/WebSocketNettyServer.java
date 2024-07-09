package io.github.chenyilei2016.netty_basic.websocket_with_springboot.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * 可以自己处理websocket的upgrade ,  也可以WebSocketServerProtocolHandler
 * {@link WebSocketServerProtocolHandler}
 */
@Component
public class WebSocketNettyServer {
    //配置服务端NIO线程组
    private final EventLoopGroup parentGroup = new NioEventLoopGroup(); //NioEventLoopGroup extends MultithreadEventLoopGroup Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
    private final EventLoopGroup childGroup = new NioEventLoopGroup();
    private Channel futureChannel;

    public ChannelFuture bing(InetSocketAddress address) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture channelFuture = serverBootstrap
                .group(parentGroup, childGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {

                        ch.pipeline().addLast("http-codec", new HttpServerCodec());
                        ch.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
//                        ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
//                        ch.pipeline().addLast(new WebSocketServerProtocolHandler("/websocket"));
                        ch.pipeline().addLast(new WebSocketServerHandler());
                    }
                })
                .bind(address);
        this.futureChannel = channelFuture.channel();
        return channelFuture;
    }

    public void destroy() {
        if (null == futureChannel) return;
        futureChannel.close();
        parentGroup.shutdownGracefully();
        childGroup.shutdownGracefully();

    }
}
