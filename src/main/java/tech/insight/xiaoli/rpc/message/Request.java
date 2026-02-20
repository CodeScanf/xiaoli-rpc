package tech.insight.xiaoli.rpc.message;

import lombok.Data;

/**
 * Request
 *
 * @author xiaoli <lxiaobin10@gmail.com>
 * @description 请求解析
 * @date 2026/1/28
 */
@Data
public class Request {
    private String serviceName;
    private String methodName;
    private String[] parmsClass;
    private Object[] parms;
}