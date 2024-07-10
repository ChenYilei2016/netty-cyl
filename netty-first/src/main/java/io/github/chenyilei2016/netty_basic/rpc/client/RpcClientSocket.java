package io.github.chenyilei2016.netty_basic.rpc.client;

import io.github.chenyilei2016.netty_basic.tcp.client.MyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;


public class RpcClientSocket implements Runnable {

    private ChannelFuture future;

    @Override
    public void run() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {

                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    //解码的时候, 区分 length header 等 .
                    LengthFieldBasedFrameDecoder lengthFieldBasedFrameDecoder = new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4);
                    pipeline.addLast(lengthFieldBasedFrameDecoder);
                    //发送消息时 , 自带length
                    pipeline.addLast(new LengthFieldPrepender(4));
                    pipeline.addLast(
//                            new RpcDecoder(Response.class),
//                            new RpcEncoder(Request.class),
                            new MyClientHandler());
                }
            });
            ChannelFuture f = b.connect("127.0.0.1", 7397).sync();
            this.future = f;
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public ChannelFuture getFuture() {
        return future;
    }

    public void setFuture(ChannelFuture future) {
        this.future = future;
    }
}
