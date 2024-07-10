package io.github.chenyilei2016.nettysync.future;

import com.alibaba.fastjson.JSON;
import io.github.chenyilei2016.nettysync.msg.Response;
import io.netty.util.concurrent.DefaultPromise;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.LockSupport;

/**
 * 类似于 {@link DefaultPromise}
 *
 * @author chenyilei
 * @since 2024/07/10 14:06
 */
public class SyncWriteFuture<T> implements WriteFuture<T> {

    private CountDownLatch latch = new CountDownLatch(1);
    private final long begin = System.currentTimeMillis();

    private final String requestId;
    private T response;
    private boolean writeResult;
    private Throwable cause;

    private boolean isTimeout = false;
    private long timeout = 10000;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        /**
         * @see io.netty.util.concurrent.Promise
         * @see DefaultPromise
         */
        DefaultPromise<Response> responseDefaultPromise = new DefaultPromise<Response>() {

        };

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    responseDefaultPromise.setSuccess(new Response());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        System.err.println(JSON.toJSONString(responseDefaultPromise.get(), true));

    }


    public SyncWriteFuture(String requestId) {
        this.requestId = requestId;
    }

    public SyncWriteFuture(String requestId, long timeout) {
        this.requestId = requestId;
        this.timeout = timeout;
        writeResult = true;
        isTimeout = false;
    }


    @Override
    public Throwable cause() {
        return cause;
    }

    @Override
    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    public boolean isWriteSuccess() {
        return writeResult;
    }

    public void setWriteResult(boolean result) {
        this.writeResult = result;
    }

    public String requestId() {
        return requestId;
    }

    public T response() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
        latch.countDown();
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return true;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return false;
    }

    public T get() throws InterruptedException, ExecutionException {
        latch.wait();
        return response;
    }

    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (latch.await(timeout, unit)) {
            return response;
        }
        return null;
    }

    public boolean isTimeout() {
        if (isTimeout) {
            return isTimeout;
        }
        return System.currentTimeMillis() - begin > timeout;
    }
}
