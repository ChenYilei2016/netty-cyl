package io.github.chenyilei2016.nettysync.future;

import io.github.chenyilei2016.nettysync.msg.Request;
import io.github.chenyilei2016.nettysync.msg.Response;
import io.netty.channel.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author chenyilei
 * @since 2024/07/10 15:29
 */
public class SyncWrite {

    public Response writeAndSync(final Channel channel, final Request request, final long timeout) throws Exception {

        if (channel == null) {
            throw new NullPointerException("channel");
        }
        if (request == null) {
            throw new NullPointerException("request");
        }
        if (timeout <= 0) {
            throw new IllegalArgumentException("timeout <= 0");
        }

        String requestId = UUID.randomUUID().toString();
        request.setRequestId(requestId);


        SyncWriteFuture<Response> f = new SyncWriteFuture<>(requestId);

        SyncWriteMap.syncKey.put(requestId, f);

        try {
            ////同步处理 发送给服务端, 等待返回
            return doWriteAndSync(channel, request, timeout, f);
        } finally {
            SyncWriteMap.syncKey.remove(requestId);
        }

    }

    private Response doWriteAndSync(Channel channel, Request request, long timeout, SyncWriteFuture<Response> f) {

        channel.writeAndFlush(request)
                .addListener(new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture future) throws Exception {
                        f.setWriteResult(future.isSuccess());
                        f.setCause(future.cause());
                        if (!future.isSuccess()) {
                            SyncWriteMap.syncKey.remove(request.getRequestId());
                        }
                    }
                });

        try {
            Response response = f.get(timeout, TimeUnit.MILLISECONDS);
            return response;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
