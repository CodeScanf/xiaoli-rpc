package tech.insight.xiaoli.rpc;

/**
 * main
 *
 * @author lxiaobin10@gmail.com
 * @description main
 * @date 15:53 2026/1/17
 **/
public class Main {
    public static void main(String[] args) throws Exception {
        Consummer consummer = new Consummer();

        System.out.println(consummer.add(1,2));
        System.out.println(consummer.add(22,23));
    }
}
