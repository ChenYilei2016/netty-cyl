package io.github.chenyilei2016.nettyheartbeat.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

/**
 * @author chenyilei
 * @since 2024/07/10 16:46
 */
public class NettyClient {


    /**
     * 仅供参考 :   inactive 的时候可以考虑重连
     */

    public static void main(String[] args) throws InterruptedException {
        new NettyClient().connect("127.0.0.1", 7397);
    }

    public void connect(String inetHost, int inetPort) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel channel) throws Exception {
                    // 基于换行符号
                    channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
                    // 解码转String，注意调整自己的编码格式GBK、UTF-8
                    channel.pipeline().addLast(new StringDecoder(Charset.forName("GBK")));
                    // 解码转String，注意调整自己的编码格式GBK、UTF-8
                    channel.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
                    // 在管道中添加我们自己的接收数据实现方法
                    channel.pipeline().addLast(new MyClientHandler());
                }
            });
            ChannelFuture f = b.connect(inetHost, inetPort).sync();
            f.addListener(new ReConnectFutureListener(this)); //添加监听，处理重连
            Channel channel = f.channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }

        Thread.sleep(50000L);
    }

}

