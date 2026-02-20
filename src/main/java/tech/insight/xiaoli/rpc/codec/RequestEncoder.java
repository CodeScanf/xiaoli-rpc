package tech.insight.xiaoli.rpc.codec;

import com.alibaba.fastjson2.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import tech.insight.xiaoli.rpc.message.Message;
import tech.insight.xiaoli.rpc.message.Request;

import java.nio.charset.StandardCharsets;

/**
 * RequestEncoder
 *
 * @author xiaoli <lxiaobin10@gmail.com>
 * @description 请求编码器
 * @date 2026/1/29
 */
public class RequestEncoder extends MessageToByteEncoder<Request> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Request request, ByteBuf out) throws Exception {
        // length + magic + messageType + body
        byte[] magic = Message.MAGIC;
        byte messageType = Message.MessageType.REQUEST.getCode();
        byte[] body = serializeRequest(request);
        int length = magic.length + Byte.BYTES + body.length;
        out.writeInt(length);
        out.writeBytes(magic);
        out.writeByte(messageType);
        out.writeBytes(body);
    }

    private byte[] serializeRequest(Request request) {
        return JSONObject.toJSONString(request).getBytes(StandardCharsets.UTF_8);
    }
}