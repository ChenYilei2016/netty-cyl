package io.github.chenyilei2016.nettycluster;

import redis.embedded.RedisServer;

public class ERedis {

    public static void startAndFinallyShutdown(Runnable runnable) {
        RedisServer redisServer = RedisServer.builder()
//                .setting("maxheap 200m")
                .setting("bind localhost")
                .setting("timeout 0")
                .port(6379)
                .build();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (redisServer.isActive()) {
                    redisServer.stop();
                }
            }
        });

        try {
            redisServer.start();
            Thread.sleep(500L);
            runnable.run();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            redisServer.stop();
        }


    }
}
