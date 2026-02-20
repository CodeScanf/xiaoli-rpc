package tech.insight.xiaoli.rpc.codec;

import com.alibaba.fastjson2.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import tech.insight.xiaoli.rpc.message.Message;
import tech.insight.xiaoli.rpc.message.Request;
import tech.insight.xiaoli.rpc.message.Response;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

/**
 * XLDecoder
 *
 * @author xiaoli <lxiaobin10@gmail.com>
 * @description 解码器
 * @date 2026/1/25
 */
public class XLDecoder extends LengthFieldBasedFrameDecoder {

    public XLDecoder() {
        super(1024 * 1024, 0, 4, 0, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);  //帧解码器,拿到的一个帧

        byte[] magic = new byte[Message.MAGIC.length];
        frame.readBytes(magic);
        // 校验魔数
        if (!Arrays.equals(magic, Message.MAGIC)) {
            throw new IllegalArgumentException("Invalid magic number, 协议有问题");
        }
        byte messageType = frame.readByte();
        byte[] body = new byte[frame.readableBytes()];
        frame.readBytes(body);      // 读取消息体, 对象序列化后的内容

        if (Objects.equals(Message.MessageType.REQUEST.getCode(), messageType)) {
            return deserializeRequest(body);
        }
        if (Objects.equals(Message.MessageType.RESPONSE.getCode(), messageType)) {
            return deserializeResponse(body);
        }
        throw new IllegalArgumentException("Unknown message type: " + messageType);
    }

    private Request deserializeRequest(byte[] body) {
        return JSONObject.parseObject(new String(body, StandardCharsets.UTF_8), Request.class);
    }

    private Response deserializeResponse(byte[] body) {
        return JSONObject.parseObject(new String(body, StandardCharsets.UTF_8), Response.class);
    }
}