package io.github.chenyilei2016.nettyheartbeat.client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

/**
 * @author chenyilei
 * @since 2024/07/10 20:32
 */
public class ReConnectFutureListener implements ChannelFutureListener {
    private NettyClient nettyClient;

    public ReConnectFutureListener(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        /**
         * 这里是第一次 连接的时候如果失败尝试一下重连...
         */
        if (channelFuture.isSuccess()) {
            System.out.println("itstack-demo-netty client start done.");
            return;
        }

        EventLoop eventExecutors = channelFuture.channel().eventLoop();

        eventExecutors.schedule(() -> {
            try {
                new NettyClient().connect("127.0.0.1", 7397);
                System.out.println("itstack-demo-netty client start done.");
                Thread.sleep(500);
            } catch (Exception e) {
                System.out.println("itstack-demo-netty client start error go reconnect ...");
            }

        }, 1, TimeUnit.SECONDS);

    }
}
