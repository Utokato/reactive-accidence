package cn.utokato.feign.proxy;

import cn.utokato.feign.ApiServer;
import cn.utokato.feign.bean.MethodInfo;
import cn.utokato.feign.bean.ServerInfo;
import cn.utokato.feign.handler.WebClientRestHandler;
import cn.utokato.feign.interfaces.ProxyCreator;
import cn.utokato.feign.interfaces.RestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 使用 JDK 动态代理创建 代理类
 *
 * @author lma
 * @date 2019/11/24
 */
@Slf4j
public class JdkProxyCreator implements ProxyCreator {
    @Override
    public Object createProxy(Class<?> type) {
        log.info("createProxy: " + type);
        // 根据接口得到 API 服务器的信息
        ServerInfo serverInfo = extractServerInfo(type);

        log.info("serverInfo: " + serverInfo);
        // 为每一个代理类做一个rest实现
        RestHandler restHandler = new WebClientRestHandler();

        // 初始化服务器信息(初始化webclient)
        restHandler.init(serverInfo);


        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{type}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 根据方法和参数得到调用信息
                MethodInfo methodInfo = extractMethodInfo(method, args);
                log.info("methodInfo: " + methodInfo);
                // 调用 rest
                return restHandler.invokeRest(methodInfo);
            }

            /**
             * 根据方法定义和调用参数获取到调用的详细信息
             */
            private MethodInfo extractMethodInfo(Method method, Object[] args) {
                MethodInfo methodInfo = new MethodInfo();
                extractUrlAndMethod(method, methodInfo);
                extractRequestParamAndBody(method, args, methodInfo);
                extractReturnInfo(method, methodInfo);
                return methodInfo;
            }
        });
    }

    /**
     * 提取返回对象的信息
     */
    private void extractReturnInfo(Method method, MethodInfo methodInfo) {
        // 返回flux还是mono
        // isAssignableFrom 判断类型是否是某个子类
        // instanceof 判断实例是否是某个子类
        boolean isFlux = method.getReturnType().isAssignableFrom(Flux.class);
        methodInfo.setFlux(isFlux);

        // 获取返回对象的实际类型
        Class<?> elementType = extractElementType(method.getGenericReturnType());
        methodInfo.setReturnElementType(elementType);
    }

    /**
     * 获取泛型类型的实际类型
     */
    private Class<?> extractElementType(Type genericReturnType) {
        Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
        return (Class<?>) actualTypeArguments[0];
    }

    /**
     * 获取调用的参数和body
     */
    private void extractRequestParamAndBody(Method method, Object[] args, MethodInfo methodInfo) {
        // 获取方法调用的参数
        Parameter[] parameters = method.getParameters();
        // 参数和值对应的map
        Map<String, Object> params = new LinkedHashMap<>();
        methodInfo.setParams(params);
        for (int i = 0; i < parameters.length; i++) {
            PathVariable pathVariableAnno = parameters[i].getAnnotation(PathVariable.class);
            if (pathVariableAnno != null) {
                params.put(pathVariableAnno.value(), args[i]);
            }

            RequestBody requestBodyAnno = parameters[i].getAnnotation(RequestBody.class);
            if (requestBodyAnno != null) {
                methodInfo.setBody((Mono<?>) args[i]);
                // 请求对象的实际类型
                methodInfo.setBodyElementType(extractElementType(parameters[i].getParameterizedType()));
            }

        }
    }

    /**
     * 获取请求URL 和请求方法
     */
    private void extractUrlAndMethod(Method method, MethodInfo methodInfo) {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof GetMapping) {
                GetMapping a = (GetMapping) annotation;
                methodInfo.setUrl(a.value().length > 0 ? a.value()[0] : "/");
                methodInfo.setMethod(HttpMethod.GET);
            } else if (annotation instanceof PostMapping) {
                PostMapping a = (PostMapping) annotation;
                methodInfo.setUrl(a.value().length > 0 ? a.value()[0] : "/");
                methodInfo.setMethod(HttpMethod.POST);
            } else if (annotation instanceof DeleteMapping) {
                DeleteMapping a = (DeleteMapping) annotation;
                methodInfo.setUrl(a.value().length > 0 ? a.value()[0] : "/");
                methodInfo.setMethod(HttpMethod.DELETE);
            } else if (annotation instanceof PutMapping) {
                PutMapping a = (PutMapping) annotation;
                methodInfo.setUrl(a.value().length > 0 ? a.value()[0] : "/");
                methodInfo.setMethod(HttpMethod.PUT);
            }
        }
    }

    /**
     * 从注解中提取服务器信息
     * url 地址
     */
    private ServerInfo extractServerInfo(Class<?> type) {
        ServerInfo serverInfo = new ServerInfo();
        ApiServer apiServerAnnotation = type.getAnnotation(ApiServer.class);
        serverInfo.setUrl(apiServerAnnotation.value());
        return serverInfo;
    }
}
