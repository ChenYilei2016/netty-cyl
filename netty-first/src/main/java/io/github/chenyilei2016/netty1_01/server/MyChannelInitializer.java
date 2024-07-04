package io.github.chenyilei2016.netty1_01.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 对一个channel 进行初始化
 *
 * @author chenyilei
 * @since 2024/07/02 17:46
 */
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    static ByteBuf byteBuf = Unpooled.wrappedBuffer("-".getBytes()); // - 作为结尾

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {

        channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, false, byteBuf));

//        channel.pipeline().addLast(new LineBasedFrameDecoder(1024));
//        channel.pipeline().addLast(new FixedLengthFrameDecoder(4));
        channel.pipeline().addLast(new StringDecoder(StandardCharsets.UTF_8));
        channel.pipeline().addLast(new StringEncoder(StandardCharsets.UTF_8));

        //在管道中添加我们自己的接收数据实现方法
        channel.pipeline().addLast(new MyServerHandler());

    }
}
