package io.github.chenyilei2016.netty1_01.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author chenyilei
 * @since 2024/07/04 11:19
 */
public class NettyClient {

    public static void main(String[] args) throws InterruptedException {

        connect("localhost", 7777);

    }

    private static void connect(String host, int port) throws InterruptedException {
        EventLoopGroup loopGroup = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        try {
            ChannelFuture connect = bootstrap.group(loopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.AUTO_READ, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {


                            // 基于换行符号
                            channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                            // 解码转String，注意调整自己的编码格式GBK、UTF-8
                            channel.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
                            // 解码转String，注意调整自己的编码格式GBK、UTF-8
                            channel.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));
                            channel.pipeline().addLast(new MyClientHandler());


                        }
                    }).connect(host, port);
            connect.sync();
            connect.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
//            loopGroup.shutdownGracefully();
        }

    }
}
