package io.github.chenyilei2016.nettycluster.util;

import io.github.chenyilei2016.nettycluster.domain.ServerInfo;
import io.github.chenyilei2016.nettycluster.web.NettyServer;
import io.netty.channel.Channel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class CacheUtil {

    // 缓存channel
    public static Map<String, Channel> cacheChannel = Collections.synchronizedMap(new HashMap<String, Channel>());

    // 缓存服务信息
    public static Map<Integer, ServerInfo> serverInfoMap = Collections.synchronizedMap(new HashMap<Integer, ServerInfo>());

    // 缓存服务端
    public static Map<Integer, NettyServer> serverMap = Collections.synchronizedMap(new HashMap<Integer, NettyServer>());

}
