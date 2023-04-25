package alibaba.dumy.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

@Component
public class NettyClient extends ChannelInboundHandlerAdapter {

    private Channel channel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        channel = ctx.channel();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        String message = buf.toString(CharsetUtil.UTF_8);

        System.out.println(message);
    }


    public void sendMessage(String message) throws InterruptedException {
        ByteBuf buf = Unpooled.copiedBuffer(message.getBytes());
        channel.writeAndFlush(buf);
    }

}
