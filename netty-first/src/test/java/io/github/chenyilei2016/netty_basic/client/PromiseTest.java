package io.github.chenyilei2016.netty_basic.client;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author chenyilei
 * @since 2024/07/12 16:37
 */
public class PromiseTest {

    @Test
    public void test1() {
        NioEventLoopGroup loop = new NioEventLoopGroup();
        // 创建一个DefaultPromise并返回，将业务逻辑放入线程池中执行
        DefaultPromise<String> promise = new DefaultPromise<String>(loop.next()) {
        };
        promise.addListener(future -> {
            System.err.println("isSuccess " +future.isSuccess());
            System.err.println("callback " + future.get());
        });

//        promise.setSuccess("11");
        promise.tryFailure(new IllegalStateException("AAAAAAAAAAAAAAAA"));

    }

}
