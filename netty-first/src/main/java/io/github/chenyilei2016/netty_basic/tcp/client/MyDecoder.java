package io.github.chenyilei2016.netty_basic.tcp.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * MyDecoder.java *用于处理解码，02开始  长度 ,  数据    03结束
 * <p>
 * <p>
 * 0x30  ==> 0
 * 0x34  ==> 4
 *
 * @author chenyilei
 * @since 2024/07/04 14:52
 */
public class MyDecoder extends ByteToMessageDecoder {

    //数据包基础长度
    private final int BASE_LENGTH = 4;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        int i = in.readableBytes();

        if (i <= 0) {
            return;
        }

        //兼容 黏包 半包

        int beginIdx; //记录包头位置
        while (true) {
            // 获取包头开始的index
            beginIdx = in.readerIndex();
            in.markReaderIndex();

            byte nextByte = in.readByte();
            if (nextByte == 0x02) {
                break;
            }
            in.resetReaderIndex();
            if (in.readableBytes() < BASE_LENGTH) {
                return;
            }
        }

        int readableBytes = in.readableBytes();

        if (readableBytes < BASE_LENGTH) {
            in.readerIndex(beginIdx);
            return;
        }

        in.readByte(); //读取0x02
        ByteBuf byteBuf = in.readBytes(1);//数据长度
        String lengthStr = byteBuf.toString(StandardCharsets.UTF_8);
        int length = Integer.parseInt(lengthStr);
        ByteBuf data = in.readBytes(length);
        byte end = in.readByte();
        if (end != 0x03) {
            in.readerIndex(beginIdx);
            return;
        }
        out.add(data.toString(StandardCharsets.UTF_8));
    }

    public static void main(String[] args) {

    }
}
