package io.github.chenyilei2016.nettychunkstream;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.stream.ChunkedStream;
import io.netty.util.ReferenceCountUtil;

import java.util.Arrays;

/**
 * 转化为已存在的JDK类型
 * Byte Array
 * 假如一个ByteBuf是有一个byte数组作为支持的, 你可以直接通过array()方法访问它。判断一个buffer是否是被byte array作为支持,调用hasArray()
 * 只有堆内内存的ByteBuf是有array支持的, 如果是堆外内存的ByteBuf, 是不能通过array()获取到数据的, 而CompositeByteBuf可能由堆内的ByteBuf和堆外的DirectByteBuf组成, 所以它也不能直接通过array()获取数据
 * NIO Buffers
 * 如果一个ByteBuf可以被转换为NIO ByteBuffer,它共享它的内容,你可以通过nioBuffer()获取它。判断一个buffer能否被转化为NIO buffer, 使用nioBufferCount().
 *
 * Strings
 * 各种各样的toString(Charset)方法将一个ByteBuf转化为一个String.请注意toString()并不是一个转换方法.
 *
 * I/O Streams
 * 请看ByteBufInputStream和ByteBufOutputStream
 */
public class MyServerChunkHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof ByteBuf)) {
            super.write(ctx, msg, promise);
            return;
        }

        ByteBuf b = (ByteBuf) msg;
        //获取byteBuf的数据
        boolean hasData = b.hasArray();
        byte[] data = null;
        if (hasData) {
            data = b.array().clone();
        } else {
            data = new byte[b.readableBytes() - 1];
            b.readBytes(data);
        }

        ByteInputStream in = new ByteInputStream();
        in.read(data);

        ChunkedStream stream = new ChunkedStream(in, 10);
        //管道消息传输承诺, 监控
        ChannelProgressivePromise progressivePromise = ctx.channel().newProgressivePromise();
        progressivePromise.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
                System.out.println("operationProgressed : " + progress + "-" + total);
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("消息发送成功 success");
                    promise.setSuccess();
                } else {
                    System.out.println("消息发送失败 failure：" + future.cause());
                    promise.setFailure(future.cause());
                }
            }
        });
        ReferenceCountUtil.release(msg);
        ctx.write(stream, progressivePromise);
    }

    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.wrappedBuffer(new byte[]{100, 101, 102, 103, 104});

        System.err.println(byteBuf);
        System.err.println(Arrays.toString(byteBuf.array()) + byteBuf.hasArray()); //[100, 101, 102, 103, 104]

        byteBuf.readByte();
        System.err.println(byteBuf);
        System.err.println(Arrays.toString(byteBuf.array()) + byteBuf.hasArray()); //[100, 101, 102, 103, 104]


        System.err.println("================================");

        /**
         * 不能直接转换成 array
         * UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeNoCleanerDirectByteBuf(ridx: 0, widx: 5, cap: 10)
         * Exception in thread "main" java.lang.UnsupportedOperationException: direct buffer
         * 	at io.netty.buffer.UnpooledDirectByteBuf.array(UnpooledDirectByteBuf.java:198)
         * 	at io.github.chenyilei2016.nettychunkstream.MyServerChunkHandler.main(MyServerChunkHandler.java:70)
         */
        ByteBuf dByteBuf = Unpooled.directBuffer(10);
        dByteBuf.writeBytes(new byte[]{100, 101, 102, 103, 104});

        System.err.println(dByteBuf);
        System.err.println(Arrays.toString(dByteBuf.array()) + dByteBuf.hasArray()); //[100, 101, 102, 103, 104]

        dByteBuf.readByte();
        System.err.println(dByteBuf);
        System.err.println(Arrays.toString(dByteBuf.array()) + dByteBuf.hasArray()); //[100, 101, 102, 103, 104]

    }
}
