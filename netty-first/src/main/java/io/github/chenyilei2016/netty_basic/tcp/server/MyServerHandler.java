package io.github.chenyilei2016.netty_basic.tcp.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

public class MyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println("链接报告开始");
        System.out.println("链接报告信息：有一客户端链接到本服务端");
        System.out.println("getHostName:" + channel.localAddress().getHostName());
        System.out.println("getHostString:" + channel.localAddress().getHostString());
        System.out.println("链接报告Port:" + channel.localAddress().getPort());

        System.out.println("r getHostName:" + channel.remoteAddress().getHostName());
        System.out.println("r getHostString:" + channel.remoteAddress().getHostString());
        System.out.println("r 链接报告Port:" + channel.remoteAddress().getPort());

        System.out.println("链接报告完毕");

        MyChannelHandler.channelGroup.add(channel);
        ctx.write("连接MyServerHandler 成功");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.err.println(" inactive " + channel.remoteAddress().getHostString());

        MyChannelHandler.channelGroup.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.err.println("MyServerHandler 接收到消息: " + msg);

//        ByteBuf byteBuf = Unpooled.wrappedBuffer("haha responser".getBytes());
//        ctx.writeAndFlush(byteBuf);

        String sendMsg = "测试发送一个字符串";
        ctx.writeAndFlush(sendMsg);

        MyChannelHandler.channelGroup.writeAndFlush(sendMsg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        System.out.println("异常信息：\r\n" + cause.getMessage());
    }

}
