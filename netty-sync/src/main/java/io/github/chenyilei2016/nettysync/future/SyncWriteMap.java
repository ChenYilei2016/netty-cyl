package io.github.chenyilei2016.nettysync.future;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenyilei
 * @since 2024/07/10 15:18
 */
public class SyncWriteMap {
    /**
     * 管理 future
     */
    public static Map<String, WriteFuture> syncKey = new ConcurrentHashMap<String, WriteFuture>();

}
