package cn.utokato.webflux;

import cn.utokato.webflux.model.IEvent;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * 测试双向流
 */
public class IEventTest {

    @Test
    public void testIEvent() {
        // 速度为每秒一个IEvent元素的数据流，不加take的话表示无限个元素的数据流；
        Flux<IEvent> eventFlux = Flux.interval(Duration.ofSeconds(1))
                .map(l -> new IEvent(System.currentTimeMillis(), "messageagainagain" + l))
                .take(5);
        WebClient webClient = WebClient.create("http://localhost:8080");
        webClient.post().uri("/events")
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(eventFlux, IEvent.class)
                .retrieve()
                .bodyToMono(Void.class) // body方法设置请求体的数据
                .block();
    }
}
