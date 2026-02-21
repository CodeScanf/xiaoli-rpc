package tech.insight.xiaoli.rpc.provider;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import tech.insight.xiaoli.rpc.message.Request;
import tech.insight.xiaoli.rpc.codec.ResponseEncoder;
import tech.insight.xiaoli.rpc.codec.XLDecoder;
import tech.insight.xiaoli.rpc.message.Response;

/**
 * Provider
 *
 * @author: lxiaobin10@gmail.com
 * @description: 生产者
 * @date: 15:55 2026/1/17
 **/
public class ProviderServer {

    private final int port;
    private EventLoopGroup bossEventLoopGroup;
    private EventLoopGroup workerEventLoopGroup;

    private ProviderRegistry registry = new ProviderRegistry();

    public ProviderServer(int port) {
        this.port = port;
    }

    public <I> void registerService(Class<I> interfaceClass, I serviceInstance) {
        registry.register(interfaceClass, serviceInstance);
    }

    public void start() {
        bossEventLoopGroup = new NioEventLoopGroup();
        workerEventLoopGroup = new NioEventLoopGroup(4);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 需要线程池，bossGroup --处理连接事件  workerGroup --处理读写事件
            serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                            nioSocketChannel.pipeline()
                                    .addLast(new XLDecoder())
                                    .addLast(new ResponseEncoder())       // bytes -> string -> handler
                                    .addLast(new SimpleChannelInboundHandler<Request>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext channelHandlerContext, Request request) throws Exception {
                                            ProviderRegistry.Invocation<?> service = registry.findService(request.getServiceName());

                                            Object result = service.invoke(request.getMethodName(), request.getParmsClass(), request.getParms());
                                            Response response = new Response();
                                            response.setResult(result);
                                            channelHandlerContext.channel().writeAndFlush(response);
                                        }
                                    });
                        }
                    });
            serverBootstrap.bind(port).sync();
        } catch (Exception e) {
            throw new RuntimeException("服务器启动异常", e);
        }
    }

    public void stop() {
        if (bossEventLoopGroup != null) {
            bossEventLoopGroup.shutdownGracefully();
        }
        if (workerEventLoopGroup != null) {
            workerEventLoopGroup.shutdownGracefully();
        }
    }

}
