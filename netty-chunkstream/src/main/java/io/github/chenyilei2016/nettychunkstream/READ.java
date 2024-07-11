package io.github.chenyilei2016.nettychunkstream;

/**
 * 在Netty这种异步NIO框架的结构下，服务端与客户端通信过程中，高效、频繁、大量的写入大块数据时
 * ，因网络传输饱和的可能性就会造成数据处理拥堵、GC频繁、用户掉线的可能性。那么由于写操作是非阻塞的，所以即使没有写出所有的数据，
 * 写操作也会在完成时返回并通知ChannelFuture。当这种情况发生时，如果仍然不停地写入，就有内存耗尽的风险。所以在写大块数据时，需要对大块数据进行切割发送处理。
 *
 * @author chenyilei
 * @since 2024/07/11 20:35
 */
public class READ {
}
