package io.github.chenyilei2016.nettychunkstream;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.stream.ChunkedStream;

public class MyServerChunkHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof ByteBuf))
            return;
        ByteBuf b = (ByteBuf) msg;
        super.write(ctx, new ChunkedStream(b.ge, 10), promise);
    }
}
