package tech.insight.xiaoli.rpc.message;

import lombok.Data;

import java.nio.charset.StandardCharsets;

/**
 * Message
 *
 * @author xiaoli <lxiaobin10@gmail.com>
 * @description 协议消息
 * @date 2026/1/25
 */
@Data
public class Message {
    public static final byte[] MAGIC = "XIAOLI".getBytes(StandardCharsets.UTF_8);
    private byte[] magic;
    private byte messageType;
    private byte[] body;

    public enum MessageType {
        REQUEST(1),
        RESPONSE(2);

        private final byte code;

        MessageType(int code) {
            this.code = (byte) code;
        }

        public byte getCode() {
            return code;
        }
    }
}