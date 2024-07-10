package io.github.chenyilei2016.nettysync.client;

import io.github.chenyilei2016.nettysync.future.SyncWriteMap;
import io.github.chenyilei2016.nettysync.future.WriteFuture;
import io.github.chenyilei2016.nettysync.msg.Request;
import io.github.chenyilei2016.nettysync.msg.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author chenyilei
 * @since 2024/07/10 10:58
 */
public class MyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof Response)) {
            return;
        }

        Response response = (Response) msg;

        WriteFuture writeFuture = SyncWriteMap.syncKey.get(response.getRequestId());

        if (writeFuture != null) {
            writeFuture.setResponse(response);
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
