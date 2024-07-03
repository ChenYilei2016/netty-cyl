package io.github.chenyilei2016.netty1_01.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author chenyilei
 * @since 2024/07/02 17:47
 */
public class NettyServer {

    public static void main(String[] args) throws InterruptedException {

        EventLoopGroup parent = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(parent)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childHandler(new MyChannelInitializer());

            ChannelFuture bind = serverBootstrap.bind(7777);
            ChannelFuture channelFuture = bind.sync();
            channelFuture.channel().closeFuture().sync();
//            channelFuture.channel().closeFuture().sync();
        } finally {
            worker.shutdownGracefully();
            parent.shutdownGracefully();
        }


    }
}
