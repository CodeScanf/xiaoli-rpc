package tech.insight.xiaoli.rpc.provider;

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
        providerServer.start();
    }
}