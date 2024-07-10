package io.github.chenyilei2016.netty_basic.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class UdpNettyClient {

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new SimpleChannelInboundHandler<DatagramPacket>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
                                    String msg = packet.content().toString(StandardCharsets.UTF_8);
                                    System.err.println(Thread.currentThread() + "client 收到 :" +msg);
                                }
                            });
                        }
                    });
            Channel ch = b.bind(8888).sync().channel();
            //向目标端口发送信息
            for (int i = 0; i < 20; i++) {
                ch.writeAndFlush(new DatagramPacket(
                        Unpooled.copiedBuffer("我是client , 我发了一个消息", StandardCharsets.UTF_8),
                        new InetSocketAddress("127.0.0.1", 9999))).sync();
            }

            ch.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
