package alibaba.controller;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NettyServerConfig {

    private final NettyServerInitializer nettyServerInitializer;
    @Autowired
    public NettyServerConfig(NettyServerInitializer nettyServerInitializer) {
        this.nettyServerInitializer = nettyServerInitializer;
    }

    @Value("${netty.server.port}")
    private int port;

    @Bean
    public ServerBootstrap serverBootstrap() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(nettyServerInitializer);
        bootstrap.bind(port).sync();
        System.out.println("Netty server started at port " + port);
        return bootstrap;
    }

}
