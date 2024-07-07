package io.github.chenyilei2016.netty_basic.tcp.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * 为什么ChannelOutboundHandler会有read方法
 * 为什么ChannelOutboundHandler会有read方法
 * 事实上，ChannelOutboundHandler的read方法更像是用来“触发”一个io read事件，而不是用来“处理”一个io read事件，也就是说，当你调用ChannelOutboundHandler.read方法时，会触发channel去向selector读取数据，channel读取到数据后，就会将数据交给ChannelPipeline，ChannelPipeline会触发一个channelRead事件，并且传递读取到的数据，这样ChannelInboundHandler就可以在channelRead方法中读取到这些数据了。
 *
 * auto-read参数的意义
 * 但是，大多数情况下，每当有数据写入时，
 * 注册到NioSocketChannel的ChannelInboundHandler的channelRead方法就自动被调用了，
 * 我们并没有主动去调用ChannelOutboundHandler的read方法去“触发”一个io read事件。这是怎么回事呢？

 */
public class MyOutMsgHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("ChannelOutboundHandlerAdapter.read 发来一条消息\r\n");
        super.read(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.writeAndFlush("ChannelOutboundHandlerAdapter.write 发来一条消息\r\n");
        super.write(ctx, msg, promise);
    }

}