package io.github.chenyilei2016.nettysync.server;

import io.github.chenyilei2016.nettysync.codec.RpcDecoder;
import io.github.chenyilei2016.nettysync.codec.RpcEncoder;
import io.github.chenyilei2016.nettysync.msg.Request;
import io.github.chenyilei2016.nettysync.msg.Response;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;

public class ServerSocket implements Runnable {

    private ChannelFuture f;

    public static void main(String[] args) throws InterruptedException {
        new Thread(new ServerSocket()).start();
        Thread.sleep(500000);
    }

    @Override
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(
                                    new RpcDecoder(Request.class),
                                    new RpcEncoder(Response.class),
                                    new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelRead(ChannelHandlerContext ctx, Object r) throws Exception {
                                            Request msg = (Request) r;
                                            //反馈
                                            Response request = new Response();
                                            request.setRequestId(msg.getRequestId());
                                            request.setParam(msg.getResult() + " 请求成功，" + Thread.currentThread());
                                            ctx.writeAndFlush(request);
                                            //释放 没什么意义
                                            ReferenceCountUtil.release(msg);
                                        }
                                    });
                        }
                    });

            ChannelFuture f = null;
            f = b.bind(7397).sync();
            f.channel().closeFuture().sync();


        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

}
