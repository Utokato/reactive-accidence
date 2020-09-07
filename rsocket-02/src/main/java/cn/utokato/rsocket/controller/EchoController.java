package cn.utokato.rsocket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

/**
 * Request - Response
 *
 * @author lma
 * @date 2020/03/16
 */
@Controller
public class EchoController {

    @MessageMapping("echo")
    public Mono<String> echo(String input) {
        return Mono.just("ECHO >> " + input);
    }
}
