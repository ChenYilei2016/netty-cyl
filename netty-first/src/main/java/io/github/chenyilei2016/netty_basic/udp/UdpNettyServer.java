package io.github.chenyilei2016.netty_basic.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class UdpNettyServer {

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup parent = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();

        bootstrap
                .channel(NioDatagramChannel.class)
                .group(parent)
                .option(ChannelOption.SO_BROADCAST, true)    //广播
                .option(ChannelOption.SO_RCVBUF, 2048 * 1024)// 设置UDP读缓冲区为2M
                .option(ChannelOption.SO_SNDBUF, 1024 * 1024)// 设置UDP写缓冲区为1M
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel ch) throws Exception {
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<DatagramPacket>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
                                String msg = packet.content().toString(StandardCharsets.UTF_8);
                                System.err.println("server 接收udp : " + msg);

                                ByteBuf byteBuf = Unpooled.wrappedBuffer(("server send " + LocalDateTime.now().toString()).getBytes(StandardCharsets.UTF_8));
                                DatagramPacket toSend = new DatagramPacket(byteBuf, packet.sender());
                                ctx.writeAndFlush(toSend);
                            }
                        });
                    }
                });
        ChannelFuture sync = bootstrap.bind(9999).sync();

    }


}
