package io.github.chenyilei2016.netty_basic.tcp.server;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author chenyilei
 * @since 2024/07/04 10:56
 */
public class MyChannelHandler {
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
