package tech.insight.xiaoli.rpc.provider;

import tech.insight.xiaoli.rpc.api.Add;

/**
 * ProviderApp
 *
 * @author xiaoli <lxiaobin10@gmail.com>
 * @description ProviderApp
 * @date 2026/2/20
 */
public class ProviderApp {
    public static void main(String[] args) {
        ProviderServer providerServer = new ProviderServer(8888);
        providerServer.registerService(Add.class, new AddImpl());
        providerServer.start();
    }
}