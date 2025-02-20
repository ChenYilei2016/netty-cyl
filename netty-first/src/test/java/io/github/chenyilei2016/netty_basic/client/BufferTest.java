package io.github.chenyilei2016.netty_basic.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

/**
 * @author chenyilei
 * @since 2024/07/04 15:05
 */
public class BufferTest {

    @Test
    public void t(){
        ByteBuf byteBuf = Unpooled.buffer(0 + 2);

        byteBuf.writeBytes(new byte[]{0x02});
        byteBuf.writeBytes(new byte[]{0x03});
        System.err.println(byteBuf);
        byteBuf.writeInt(12);
        System.err.println(byteBuf);


        System.err.println(new byte[]{0x03}.length);
        System.err.println(new Integer(64).byteValue());
        //int 转 字节

    }
}
