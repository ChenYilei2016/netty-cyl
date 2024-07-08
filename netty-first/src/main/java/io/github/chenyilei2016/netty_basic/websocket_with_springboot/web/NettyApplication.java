package io.github.chenyilei2016.netty_basic.websocket_with_springboot.web;

import io.github.chenyilei2016.netty_basic.websocket_with_springboot.server.WebSocketNettyServer;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.net.InetSocketAddress;

@SpringBootApplication(scanBasePackages = {"io.github.chenyilei2016.netty_basic.websocket_with_springboot"})
public class NettyApplication implements CommandLineRunner {

    @Value("${netty.host}")
    private String host;
    @Value("${netty.port}")
    private int port;


    public static void main(String[] args) {
        SpringApplication.run(NettyApplication.class, args);
    }

    @Autowired
    private WebSocketNettyServer nettyServer;

    @Override
    public void run(String... args) throws Exception {
        InetSocketAddress address = new InetSocketAddress(host, port);
        ChannelFuture channelFuture = nettyServer.bing(address);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> nettyServer.destroy()));
        channelFuture.channel().closeFuture().syncUninterruptibly();
    }

}

