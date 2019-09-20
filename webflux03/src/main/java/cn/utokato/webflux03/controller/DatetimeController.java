package cn.utokato.webflux03.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lma
 * @date 2019/09/19
 */
@Controller
public class DatetimeController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * WebSocket 与 SSE
     * 全双工 与 半双工
     *
     * @return 当前时间的字符串
     */
    @ResponseBody
    @GetMapping(value = "/now", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<String> now() {
        return Flux.interval(Duration.ofMillis(1000)).map(
                i -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n\n"
        );
    }

}
