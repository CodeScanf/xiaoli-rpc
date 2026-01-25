package tech.insight.xiaoli.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.CompletableFuture;

/**
 * Consummer
 *
 * @author: lxiaobin10@gmail.com
 * @description: 消费者
 * @date: 15:55 2026/1/17
 **/
public class Consummer {

    public int add(int a, int b) throws Exception {
        CompletableFuture<Integer> addResultFuture = new CompletableFuture<>();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup(4))
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        // 客户端的处理器
                        nioSocketChannel.pipeline()
                                .addLast(new LineBasedFrameDecoder(1024))
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast(new SimpleChannelInboundHandler<String>() {
                                    @Override
                                    protected void channelRead0(io.netty.channel.ChannelHandlerContext channelHandlerContext, String s) throws Exception {
                                        int result = Integer.parseInt(s);
                                        addResultFuture.complete(result);
                                        channelHandlerContext.close();
                                    }
                                });
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect("localhost", 8888).sync();
        channelFuture.channel().writeAndFlush("add," + a + "," + b + "\n"); // 注意加上换行符
        return addResultFuture.get();

    }
}
