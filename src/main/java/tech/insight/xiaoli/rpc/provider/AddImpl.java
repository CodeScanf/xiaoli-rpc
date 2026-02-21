package tech.insight.xiaoli.rpc.provider;

import tech.insight.xiaoli.rpc.api.Add;

/**
 * AddImpl
 *
 * @author xiaoli <lxiaobin10@gmail.com>
 * @description AddImpl
 * @date 2026/2/21
 */
public class AddImpl implements Add {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}