package io.github.chenyilei2016.netty1_01.client;

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
    }
}
