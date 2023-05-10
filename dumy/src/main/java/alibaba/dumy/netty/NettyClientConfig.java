//package alibaba.dumy.netty;
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class NettyClientConfig {
//
//    private final NettyClientInitializer nettyClientInitializer;
//    @Autowired
//    public NettyClientConfig(NettyClientInitializer nettyClientInitializer) {
//        this.nettyClientInitializer = nettyClientInitializer;
//    }
//
//    @Value("${netty.server.host}")
//    private String host;
//
//    @Value("${netty.server.port}")
//    private int port;
//
//    @Bean
//    public Bootstrap bootstrap() throws InterruptedException {
//        EventLoopGroup group = new NioEventLoopGroup();
//        Bootstrap bootstrap = new Bootstrap();
//        bootstrap.group(group)
//                .channel(NioSocketChannel.class)
//                .handler(nettyClientInitializer);
//        bootstrap.connect(host, port).sync();
//        return bootstrap;
//    }
//
//}
