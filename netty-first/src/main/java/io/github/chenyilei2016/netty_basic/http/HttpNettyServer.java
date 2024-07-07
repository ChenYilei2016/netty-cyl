package io.github.chenyilei2016.netty_basic.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;

public class HttpNettyServer {

    public static void main(String[] args) {
        new HttpNettyServer().bing(7397);
    }

    private void bing(int port) {
        //配置服务端NIO线程组
        EventLoopGroup parentGroup = new NioEventLoopGroup(); //NioEventLoopGroup extends MultithreadEventLoopGroup Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)    //非阻塞模式
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel channel) throws Exception {
                            /**
                             *   // 解码成HttpRequest
                             *                 pipeline.addLast(new HttpServerCodec());
                             *
                             *                 // 解码成FullHttpRequest
                             *                 pipeline.addLast(new HttpObjectAggregator(1024*10));
                             */
                            // 数据解码操作
                            channel.pipeline().addLast(new HttpResponseEncoder());
                            // 数据编码操作
                            channel.pipeline().addLast(new HttpRequestDecoder());
                            channel.pipeline().addLast(new HttpObjectAggregator(10*1024));
                            // 在管道中添加我们自己的接收数据实现方法
                            channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    FullHttpRequest f = (FullHttpRequest) msg;
                                    System.err.println(f.content().toString(StandardCharsets.UTF_8));


                                    String sendMsg = "xxxxxx html detail";

                                    FullHttpResponse response = new DefaultFullHttpResponse(
                                            HttpVersion.HTTP_1_1,
                                            HttpResponseStatus.OK,
                                            Unpooled.wrappedBuffer(sendMsg.getBytes(StandardCharsets.UTF_8)));
                                    response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
                                    response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
                                    response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                                    ctx.writeAndFlush(response);
                                }
                            });
                        }
                    });
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().syncUninterruptibly();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            childGroup.shutdownGracefully();
            parentGroup.shutdownGracefully();
        }

    }

}
