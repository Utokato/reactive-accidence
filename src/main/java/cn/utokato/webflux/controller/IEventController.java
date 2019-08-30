package cn.utokato.webflux.controller;

import cn.utokato.webflux.model.IEvent;
import cn.utokato.webflux.repository.IEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/events")
public class IEventController {

    @Autowired
    private IEventRepository iEventRepository;

    /**
     * POST方法的接收数据流的Endpoint，所以传入的参数是一个Flux，返回结果其实就看需要了
     * 我们用一个Mono<Void>作为方法返回值，表示如果传输完的话只给一个“完成信号”就OK了
     *
     * @param events
     * @return
     */
    @PostMapping(path = "",consumes = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<Void> loadEvents(@RequestBody Flux<IEvent> events) {
        return this.iEventRepository.insert(events).then();
    }

    /**
     * GET方法的无限发出数据流的Endpoint，所以返回结果是一个Flux<MyEvent>
     * 不要忘了注解上produces = MediaType.APPLICATION_STREAM_JSON_VALUE。
     * @return
     */
    @GetMapping(path = "", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<IEvent> getEvents() {
        return this.iEventRepository.findBy();
    }

}
