package io.github.chenyilei2016.netty1_01.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * MyEncoder.java *用于处理编码，在byte开始和结束加上02 03
 *
 * String msg = in.toString();
 * byte[] bytes = msg.getBytes();
 *
 * byte[] send = new byte[bytes.length + 2];
 * System.arraycopy(bytes, 0, send, 1, bytes.length);
 * send[0] = 0x02;
 * send[send.length - 1] = 0x03;
 *
 * out.writeInt(send.length);
 * out.writeBytes(send);
 *
 * @author chenyilei
 * @since 2024/07/04 14:53
 */
public class MyEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

        byte[] bytes = msg.toString().getBytes();

        // 加上前后缀区分

        ByteBuf byteBuf = Unpooled.buffer(bytes.length + 2);

        byteBuf.writeBytes(new byte[]{0x02});
        byteBuf.writeBytes(bytes);
        byteBuf.writeBytes(new byte[]{0x03});

        out.writeBytes(byteBuf);
    }
}
