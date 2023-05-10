//package alibaba.dumy.netty;
//
//
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelPipeline;
//import io.netty.channel.socket.SocketChannel;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
//
//    private final NettyClient nettyClient;
//
//    @Autowired
//    public NettyClientInitializer(NettyClient nettyClient) {
//        this.nettyClient = nettyClient;
//    }
//
//    @Override
//    public void initChannel(SocketChannel ch) throws Exception {
//        ChannelPipeline pipeline = ch.pipeline();
//
//        pipeline.addLast(nettyClient);
//    }
//}
