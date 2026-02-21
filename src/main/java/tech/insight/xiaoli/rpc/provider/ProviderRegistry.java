package tech.insight.xiaoli.rpc.provider;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ProviderRegistry
 *
 * @author xiaoli <lxiaobin10@gmail.com>
 * @description 生产者注册表
 * @date 2026/2/21
 */
public class ProviderRegistry {

    private Map<String, Invocation> serviceInstanceMap = new ConcurrentHashMap<>();

    public <I> void register(Class<I> interfaceClass, I serviceInstance) {
        if ( !interfaceClass.isInterface() ) {
            throw new IllegalArgumentException("interfaceClass must be an interface");
        }
        if (serviceInstanceMap.putIfAbsent(interfaceClass.getName(), new Invocation<>(interfaceClass, serviceInstance)) != null) {
            throw new IllegalArgumentException(interfaceClass.getName() + " is already registered!");
        }
    }

    public Invocation<?> findService(String serviceName) {
        return  serviceInstanceMap.get(serviceName);
    }

    public static class Invocation<I> {
        final I serviceInstance;
        final Class<I> interfaceClass;

        public Invocation(Class<I> interfaceClass, I serviceInstance) {
            this.interfaceClass = interfaceClass;
            this.serviceInstance = serviceInstance;
        }

        public Object invoke(String methodName, Class<?>[] paramsClass, Object[] params) throws Exception {
            Method invokeMethod = interfaceClass.getMethod(methodName, paramsClass);
            return invokeMethod.invoke(serviceInstance, params);

        }
    }
}