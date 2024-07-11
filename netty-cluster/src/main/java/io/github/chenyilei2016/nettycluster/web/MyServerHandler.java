package io.github.chenyilei2016.nettycluster.web;

import io.github.chenyilei2016.nettycluster.domain.MsgAgreement;
import io.github.chenyilei2016.nettycluster.domain.UserChannelInfo;
import io.github.chenyilei2016.nettycluster.service.ExtServerService;
import io.github.chenyilei2016.nettycluster.util.CacheUtil;
import io.github.chenyilei2016.nettycluster.util.MsgUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import java.util.Date;

/**
 * @author chenyilei
 * @since 2024/07/11 18:37
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {
    private final ExtServerService extServerService;

    public MyServerHandler(ExtServerService extServerService) {
        this.extServerService = extServerService;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println("链接报告开始");
        System.out.println("链接报告信息：有一客户端链接到本服务端。channelId：" + channel.id());
        System.out.println("链接报告IP:" + channel.localAddress().getHostString());
        System.out.println("链接报告Port:" + channel.localAddress().getPort());
        System.out.println("链接报告完毕");

        //保存用户信息
        UserChannelInfo userChannelInfo = new UserChannelInfo(channel.localAddress().getHostString(), channel.localAddress().getPort(), channel.id().toString(), new Date());
        extServerService.getRedisUtil().pushObjByChannelId(userChannelInfo);
        CacheUtil.cacheChannel.put(channel.id().toString(), channel);
        //通知客户端链接建立成功
        String str = Thread.currentThread().getName() + " 通知客户端链接建立成功" + " " + new Date() + " " + channel.localAddress().getHostString() + "\r\n";
        ctx.writeAndFlush(MsgUtil.buildMsg(channel.id().toString(), str));

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketChannel channel = (SocketChannel) ctx.channel();
        System.out.println(Thread.currentThread().getName() + " 客户端断开链接" + ctx.channel().localAddress().toString());
        extServerService.getRedisUtil().remove(channel.id().toString());
        CacheUtil.cacheChannel.remove(channel.id().toString(), channel);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object objMsgJsonStr) throws Exception {
        System.err.println("收到消息: " + objMsgJsonStr);
        //是否需要转发给其他服务
        MsgAgreement msgAgreement = MsgUtil.json2Obj(objMsgJsonStr.toString());
        String toChannelId = msgAgreement.getToChannelId();
        //判断接收消息用户是否在本服务端
        Channel channel = CacheUtil.cacheChannel.get(toChannelId);
        if (null != channel) {
            channel.writeAndFlush(MsgUtil.obj2Json(msgAgreement));
            return;
        }
        //如果为NULL则接收消息的用户不在本服务端，需要push消息给全局
        System.err.println("接收消息的用户不在本服务端，PUSH！");
        extServerService.push(msgAgreement);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
    }
}
