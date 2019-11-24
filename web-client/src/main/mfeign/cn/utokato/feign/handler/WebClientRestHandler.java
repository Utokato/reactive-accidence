package cn.utokato.feign.handler;

import cn.utokato.feign.bean.MethodInfo;
import cn.utokato.feign.bean.ServerInfo;
import cn.utokato.feign.interfaces.RestHandler;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author lma
 * @date 2019/11/24
 */
public class WebClientRestHandler implements RestHandler {

    private WebClient client;

    /**
     * 初始化 webclient
     */
    @Override
    public void init(ServerInfo serverInfo) {
        this.client = WebClient.create(serverInfo.getUrl());
    }


    /**
     * 处理 rest 请求
     */
    @Override
    public Object invokeRest(MethodInfo methodInfo) {
        Object result;

        WebClient.RequestBodySpec request = this.client
                .method(methodInfo.getMethod())
                .uri(methodInfo.getUrl(), methodInfo.getParams())
                .accept(MediaType.APPLICATION_JSON);

        WebClient.ResponseSpec retrieve;
        if (methodInfo.getBody() != null) {
            retrieve = request.body(methodInfo.getBody(), methodInfo.getBodyElementType()).retrieve();
        } else {
            retrieve = request.retrieve();
        }

        // 处理异常
        retrieve.onStatus(httpStatus -> httpStatus.value() == 404,
                clientResponse -> Mono.just(new RuntimeException("Not found!")));

        if (methodInfo.isFlux()) {
            result = retrieve.bodyToFlux(methodInfo.getReturnElementType());
        } else {
            result = retrieve.bodyToMono(methodInfo.getReturnElementType());
        }

        return result;
    }
}
