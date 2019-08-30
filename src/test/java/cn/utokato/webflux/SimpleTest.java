package cn.utokato.webflux;

import cn.utokato.webflux.model.User;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * 测试webclient
 */
public class SimpleTest {

    @Test
    public void webClientTest1() throws InterruptedException {
        // 创建WebClient对象并指定baseUrl；
        WebClient webClient = WebClient.create("http://localhost:8080");

        Mono<String> resp = webClient.get().uri("/hello")// HTTP GET；
                .retrieve()// 异步地获取response信息；
                .bodyToMono(String.class); // 将response body解析为字符串；
        resp.subscribe(System.out::println);
        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void webClientTest2() throws InterruptedException {
        // 使用WebClientBuilder来构建WebClient对象；
        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build();
        //
        webClient.get().uri("/user/mall")
                .accept(MediaType.APPLICATION_STREAM_JSON)// 配置请求Header：Content-Type: application/stream+json；
                .exchange()// 获取response信息，返回值为ClientResponse，retrive()可以看做是exchange()方法的“快捷版”；
                .flatMapMany(response -> response.bodyToFlux(User.class))// 使用flatMap来将ClientResponse映射为Flux；
                .doOnNext(System.out::println) // 只读地peek每个元素，然后打印出来，它并不是subscribe，所以不会触发流；
                .blockLast(); // blockLast方法，顾名思义，在收到最后一个元素前会阻塞，响应式业务场景中慎用。
    }

    @Test
    public void webClientTest3() throws InterruptedException {
        WebClient webClient = WebClient.create("http://localhost:8080");
        webClient.get().uri("/times")
                .accept(MediaType.TEXT_EVENT_STREAM) //配置请求Header：Content-Type: text/event-stream，即SSE；
                .retrieve().bodyToFlux(String.class)
                .log() // 用log()代替doOnNext(System.out::println)来查看每个元素；
                .take(10)// 由于/times是一个无限流，这里取前10个，会导致流被取消；
                .blockLast();
    }



}
