package tech.insight.xiaoli.rpc.consumer;

import tech.insight.xiaoli.rpc.api.Add;

/**
 * ConsumerApp
 *
 * @author xiaoli <lxiaobin10@gmail.com>
 * @description ConsumerApp
 * @date 2026/2/20
 */
public class ConsumerApp {
    public static void main(String[] args) throws Exception {
        Add consummer = new Consumer();

        System.out.println(consummer.add(1, 23));
        System.out.println(consummer.add(22, 23));
    }
}