package tech.insight.xiaoli.rpc.consumer;

/**
 * ConsumerApp
 *
 * @author xiaoli <lxiaobin10@gmail.com>
 * @description ConsumerApp
 * @date 2026/2/20
 */
public class ConsumerApp {
    public static void main(String[] args) throws Exception {
        Consumer consummer = new Consumer();

        System.out.println(consummer.add(1, 2));
        System.out.println(consummer.add(22, 23));
    }
}