package io.github.chenyilei2016.netty_basic.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author qingshi
 * @email 705029004@qq.com
 * @date 2021/03/21 4:19 下午
 */
public class _01_黏包半包 {
    static class 普通的会黏包的Server {
        static final Logger log = LoggerFactory.getLogger(普通的会黏包的Server.class);

        void start() {
            NioEventLoopGroup boss = new NioEventLoopGroup(1);
            NioEventLoopGroup worker = new NioEventLoopGroup();
            try {
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                serverBootstrap.channel(NioServerSocketChannel.class);
                serverBootstrap.group(boss, worker);
                serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                log.debug("connected {}", ctx.channel());
                                super.channelActive(ctx);
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                log.debug("disconnect {}", ctx.channel());
                                super.channelInactive(ctx);
                            }
                        });
                    }
                });
                ChannelFuture channelFuture = serverBootstrap.bind(8080);
                log.debug("{} binding...", channelFuture.channel());
                channelFuture.sync();
                log.debug("{} bound...", channelFuture.channel());
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.error("server error", e);
            } finally {
                boss.shutdownGracefully();
                worker.shutdownGracefully();
                log.debug("stoped");
            }
        }
    }

    static class 缓冲区小会半包的Server {
        static final Logger log = LoggerFactory.getLogger(普通的会黏包的Server.class);

        void start() {
            NioEventLoopGroup boss = new NioEventLoopGroup(1);
            NioEventLoopGroup worker = new NioEventLoopGroup();
            try {
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                //FIXME 主要是这里, 设置全局的缓冲区大小
                serverBootstrap.option(ChannelOption.SO_RCVBUF, 10);
                serverBootstrap.channel(NioServerSocketChannel.class);
                serverBootstrap.group(boss, worker);
                serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                log.debug("connected {}", ctx.channel());
                                super.channelActive(ctx);
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                log.debug("disconnect {}", ctx.channel());
                                super.channelInactive(ctx);
                            }
                        });
                    }
                });
                ChannelFuture channelFuture = serverBootstrap.bind(8080);
                log.debug("{} binding...", channelFuture.channel());
                channelFuture.sync();
                log.debug("{} bound...", channelFuture.channel());
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                log.error("server error", e);
            } finally {
                boss.shutdownGracefully();
                worker.shutdownGracefully();
                log.debug("stoped");
            }
        }
    }

    @Test
    public void server() {
        /**
         * {@link ChannelInboundHandlerAdapter}
         * {@link ChannelOutboundHandlerAdapter}
         */
        new 普通的会黏包的Server().start();
    }

    @Test
    public void 减小接收缓冲区server2() {
        /**
         * {@link ChannelInboundHandlerAdapter}
         * {@link ChannelOutboundHandlerAdapter}
         */
        new 缓冲区小会半包的Server().start();
    }

    @Test
    public void client() {
        final Logger log = LoggerFactory.getLogger(this.getClass());
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(worker);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    log.debug("connetted...");
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            log.debug("sending...");
                            Random r = new Random();
                            char c = 'a';
                            for (int i = 0; i < 10; i++) {
                                ByteBuf buffer = ctx.alloc().buffer();
                                buffer.writeBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
                                ctx.writeAndFlush(buffer);
                            }
                        }
                    });
                }
            });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            log.error("client error", e);
        } finally {
            worker.shutdownGracefully();
        }
    }


    @Test
    public void 各种解决管道() {
        //option(ChannelOption.SO_RCVBUF, 20 * 1024 * 1024)为配置接收缓冲区，
        //option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(65535))为每次读取缓冲区最大长度。
        /**
         * {@link io.netty.handler.codec.FixedLengthFrameDecoder} 定长的
         *
         * {@link io.netty.handler.codec.LineBasedFrameDecoder} 以换行符为分割
         *
         * {@link io.netty.handler.codec.LengthFieldBasedFrameDecoder}
         *
         */
    }
}
