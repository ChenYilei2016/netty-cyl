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

import javax.annotation.Resource;
import java.net.InetSocketAddress;

@Component
public class WebSocketNettyServer {
    //配置服务端NIO线程组
    private final EventLoopGroup parentGroup = new NioEventLoopGroup(); //NioEventLoopGroup extends MultithreadEventLoopGroup Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
    private final EventLoopGroup childGroup = new NioEventLoopGroup();
    private Channel channel;

    public ChannelFuture bing(InetSocketAddress address) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture channelFuture = serverBootstrap
                .group(parentGroup, childGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        /**
                         *  channel.pipeline().addLast("http-codec", new HttpServerCodec());
                         *                         channel.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
                         *
                         *                         java.lang.IllegalArgumentException: Duplicate handler name: http-codec
                         * 	at io.netty.channel.DefaultChannelPipeline.checkDuplicateName(DefaultChannelPipeline.java:1066) [netty-all-4.1.37.Final.jar:4.1.37.Final]
                         * 	at io.netty.channel.DefaultChannelPipeline.filterName(DefaultChannelPipeline.java:284) [netty-all-4.1.37.Final.jar:4.1.37.Final]
                         * 	at io.netty.channel.DefaultChannelPipeline.addLast(DefaultChannelPipeline.java:204) [netty-all-4.1.37.Final.jar:4.1.37.Final]
                         *
                         * 	why ?? todo: xx???
                         */

                        channel.pipeline().addLast( new HttpServerCodec());
                        channel.pipeline().addLast( new HttpObjectAggregator(65536));
                        channel.pipeline().addLast(new WebSocketServerProtocolHandler("/websocket"));
//                        channel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                        channel.pipeline().addLast(new WebSocketServerHandler());
                    }
                })
                .bind(address);
        this.channel = channelFuture.channel();
        return channelFuture;
    }

    public void destroy() {
        if (null == channel) return;
        channel.close();
        parentGroup.shutdownGracefully();
        childGroup.shutdownGracefully();

    }
}
