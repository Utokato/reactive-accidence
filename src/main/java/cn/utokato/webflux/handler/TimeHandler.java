package cn.utokato.webflux.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public class TimeHandler {

    public Mono<ServerResponse> getTime(ServerRequest serverRequest) {
        return ok().contentType(TEXT_PLAIN)
                .body(Mono.just("Now is " + new SimpleDateFormat("HH:mm:ss").format(new Date())), String.class);
    }

    public Mono<ServerResponse> getDate(ServerRequest serverRequest) {
        return ok().contentType(TEXT_PLAIN)
                .body(Mono.just("Today is " + new SimpleDateFormat("yyyy-MM-dd").format(new Date())), String.class);
    }

    public Mono<ServerResponse> sendTimePerSec(ServerRequest serverRequest) {
        // MediaType.TEXT_EVENT_STREAM表示Content-Type为text/event-stream，即SSE
        return ok().contentType(TEXT_EVENT_STREAM)
                // 利用interval生成每秒一个数据的流
                .body(Flux.interval(Duration.ofSeconds(1))
                        .map(l -> new SimpleDateFormat("HH:mm:ss").format(new Date())), String.class);
    }

}
