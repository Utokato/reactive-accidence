package cn.utokato.webflux03;

import cn.utokato.webflux03.domain.MyEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Webflux03ApplicationTests {

    @Test
    public void contextLoads() {
    }

    /**
     * 注意 {@link WebClient} 的使用
     */
    @Test
    public void webClientToPostEvent() {
        Flux<MyEvent> eventFlux = Flux.interval(Duration.ofSeconds(1)).map(
                l -> new MyEvent(System.currentTimeMillis(), "message-" + l)
        );
        WebClient webClient = WebClient.create("http://localhost:7070");
        webClient.post().uri("/events")
                .contentType(MediaType.APPLICATION_STREAM_JSON)
                .body(eventFlux, MyEvent.class)
                .retrieve()
                .bodyToMono(Void.class).block();
    }

}
