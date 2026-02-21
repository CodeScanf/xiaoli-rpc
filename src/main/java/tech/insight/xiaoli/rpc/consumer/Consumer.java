package tech.insight.xiaoli.rpc.consumer;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import tech.insight.xiaoli.rpc.api.Add;
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
public class Consumer implements Add {

    @Override
    public int add(int a, int b) {
        try {
            CompletableFuture<Integer> addResultFuture = new CompletableFuture<>();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(new NioEventLoopGroup(4))
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            // 客户端的处理器
                            nioSocketChannel.pipeline()
                                    .addLast(new RequestEncoder())
                                    .addLast(new XLDecoder())
                                    .addLast(new SimpleChannelInboundHandler<Response>() {
                                        @Override
                                        protected void channelRead0(io.netty.channel.ChannelHandlerContext channelHandlerContext, Response response) throws Exception {
                                            addResultFuture.complete(Integer.valueOf(response.getResult().toString()));
                                        }
                                    });
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("localhost", 8888).sync();
            Request request = new Request();
            request.setServiceName(Add.class.getName());
            request.setMethodName("add");
            request.setParms(new Object[]{a, b});
            request.setParmsClass(new Class[]{int.class, int.class});
            channelFuture.channel().writeAndFlush(request);
            return addResultFuture.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
