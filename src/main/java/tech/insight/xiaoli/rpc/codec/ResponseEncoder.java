package tech.insight.xiaoli.rpc.codec;

import com.alibaba.fastjson2.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import tech.insight.xiaoli.rpc.message.Message;
import tech.insight.xiaoli.rpc.message.Response;

import java.nio.charset.StandardCharsets;

/**
 * RequestEncoder
 *
 * @author xiaoli <lxiaobin10@gmail.com>
 * @description 请求编码器
 * @date 2026/1/29
 */
public class ResponseEncoder extends MessageToByteEncoder<Response> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Response response, ByteBuf out) throws Exception {
        // length + magic + messageType + body
        byte[] magic = Message.MAGIC;
        byte messageType = Message.MessageType.RESPONSE.getCode();
        byte[] body = serializeResponse(response);
        int length = magic.length + Byte.BYTES + body.length;
        out.writeInt(length);
        out.writeBytes(magic);
        out.writeByte(messageType);
        out.writeBytes(body);
    }

    private byte[] serializeResponse(Response response) {
        return JSONObject.toJSONString(response).getBytes(StandardCharsets.UTF_8);
    }
}