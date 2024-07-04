package io.github.chenyilei2016.netty1_01.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * MyDecoder.java *用于处理解码，02开始 03结束
 *
 * @author chenyilei
 * @since 2024/07/04 14:52
 */
public class MyDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        int i = in.readableBytes();

        if (i <= 0) {
            return;
        }

        //兼容 黏包 半包

        byte b;
        while ((b = in.readByte()) != 0x02) {

            in.markReaderIndex()


        }

    }
}
