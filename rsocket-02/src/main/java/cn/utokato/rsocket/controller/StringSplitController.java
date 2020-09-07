package cn.utokato.rsocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

/**
 * Request - Stream
 *
 * @author lma
 * @date 2020/03/16
 */
@Controller
public class StringSplitController {

    @MessageMapping("stringSplit")
    public Flux<String> stringSplit(String input) {
        return Flux.fromStream(input.codePoints().mapToObj(c -> String.valueOf((char) c)));
    }
}
