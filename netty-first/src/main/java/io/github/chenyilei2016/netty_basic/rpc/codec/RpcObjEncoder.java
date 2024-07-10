package io.github.chenyilei2016.netty_basic.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 *
 * 头 |  消息体
 * 消息头
 * 魔数（2 btye）   消息长度（4 byte）	消息id（8 byte）   |   	消息内容（不定长）
 */

public class RpcObjEncoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

        

    }
}
