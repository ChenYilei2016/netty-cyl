package io.github.chenyilei2016.nettysync.client;

import com.alibaba.fastjson.JSON;
import io.github.chenyilei2016.nettysync.codec.RpcDecoder;
import io.github.chenyilei2016.nettysync.codec.RpcEncoder;
import io.github.chenyilei2016.nettysync.future.SyncWrite;
import io.github.chenyilei2016.nettysync.msg.Request;
import io.github.chenyilei2016.nettysync.msg.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 虫洞栈：https://bugstack.cn
 * 公众号：bugstack虫洞栈  ｛关注获取学习源码｝
 * 虫洞群：①群5398358 ②群5360692
 * Create by fuzhengwei on 2019
 */
public class ClientSocket implements Runnable {

    private ChannelFuture future;

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1000L);
        ClientSocket client = new ClientSocket();
        new Thread(client).start();

        for (int i = 0; i < 20; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            client.sendMsg();
                            Thread.sleep(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        }
    }

    private void sendMsg() throws Exception {
        //构建发送参数
        Request request = new Request();
        request.setResult("查询用户信息");
        SyncWrite s = new SyncWrite();
        Response response = s.writeAndSync(future.channel(), request, 1000);
        System.out.println("调用结果：" + JSON.toJSON(response));
    }

    @Override
    public void run() {
        EventLoopGroup workerGroup = new NioEventLoopGroup(10);
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.AUTO_READ, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            new RpcEncoder(Request.class),
                            new RpcDecoder(Response.class),
                            new MyClientHandler());
                }
            });
            ChannelFuture f = b.connect("127.0.0.1", 7397).sync();
            this.future = f;
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public ChannelFuture getFuture() {
        return future;
    }

    public void setFuture(ChannelFuture future) {
        this.future = future;
    }
}
