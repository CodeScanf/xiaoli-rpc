package tech.insight.xiaoli.rpc.consumer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import tech.insight.xiaoli.rpc.message.Request;
import tech.insight.xiaoli.rpc.codec.RequestEncoder;
import tech.insight.xiaoli.rpc.message.Response;
import tech.insight.xiaoli.rpc.codec.XLDecoder;


import java.util.concurrent.CompletableFuture;

/**
 * Consummer
 *
 * @author: lxiaobin10@gmail.com
 * @description: 消费者
 * @date: 15:55 2026/1/17
 **/
public class Consumer {

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
                                .addLast(new XLDecoder())
                                .addLast(new RequestEncoder())
                                .addLast(new SimpleChannelInboundHandler<Response>() {
                                    @Override
                                    protected void channelRead0(io.netty.channel.ChannelHandlerContext channelHandlerContext, Response response) throws Exception {
                                        System.out.println("收到响应：" + response);
                                        int result = Integer.parseInt(response.getResult().toString());
                                        addResultFuture.complete(result);
                                    }
                                });
                    }
                });
        ChannelFuture channelFuture = bootstrap.connect("localhost", 8888).sync();
        Request request = new Request();
        request.setServiceName("aaa");
        request.setMethodName("bbb");
        request.setParms(new Object[]{1, 2});
        request.setParmsClass(new String[]{"int", "int"});


        channelFuture.channel().writeAndFlush(request); // 注意加上换行符
        return addResultFuture.get();

    }
}
