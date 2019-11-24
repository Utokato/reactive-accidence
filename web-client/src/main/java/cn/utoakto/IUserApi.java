package cn.utoakto;

import cn.utokato.feign.ApiServer;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author lma
 * @date 2019/11/24
 */
@ApiServer(value = "http://localhost:7070/user")
public interface IUserApi {

    @GetMapping
    Flux<User> getAllUser();

    @GetMapping("/{id}")
    Mono<User> getUserById(@PathVariable("id") String id);

    @DeleteMapping("/{id}")
    Mono<Void> deleteUserById(@PathVariable("id") String id);

    @PostMapping
    Mono<User> createUser(@RequestBody Mono<User> user);

}
