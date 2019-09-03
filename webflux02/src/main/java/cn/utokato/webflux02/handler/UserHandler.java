package cn.utokato.webflux02.handler;

import cn.utokato.webflux02.domain.User;
import cn.utokato.webflux02.repository.UserRepository;
import cn.utokato.webflux02.util.CheckUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.server.ServerResponse.*;


/**
 * 与以前的controller起相似的作用
 *
 * @author lma
 * @date 2019/09/03
 */
@Component
public class UserHandler {

    private final UserRepository userRepository;

    @Autowired
    public UserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return ok().contentType(APPLICATION_JSON_UTF8)
                .body(userRepository.findAll(), User.class);
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {
        Mono<User> user = request.bodyToMono(User.class);
        // 不添加校验代码时：
        // return ok().contentType(APPLICATION_JSON_UTF8).body(this.repository.saveAll(user),User.class);
        // 添加校验代码时：
        return user.flatMap(u -> {
            // 将校验逻辑代码放置在这里
            // 校验失败的处理代码，最好使用异常处理类来处理，类似于AOP的想法，这样的话，逻辑代码看上去更加简洁
            CheckUtils.checkName(u.getName());
            CheckUtils.checkAge(u.getAge());
            return ok().contentType(APPLICATION_JSON_UTF8)
                    .body(userRepository.save(u), User.class);
        });
    }

    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        String id = request.pathVariable("id");
        return userRepository.findById(id)
                .flatMap(user -> userRepository.delete(user).then(ok().build()))
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> updateUser(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<User> user = request.bodyToMono(User.class);
        return user.flatMap(uu -> {
            // 先对传入的参数进行校验
            CheckUtils.checkName(uu.getName());
            CheckUtils.checkAge(uu.getAge());
            return userRepository.findById(id)
                    .flatMap(u -> {
                                // 先查询数据库，后进行修改
                                u.setName(uu.getName());
                                u.setAge(uu.getAge());
                                return ok().contentType(APPLICATION_JSON_UTF8)
                                        .body(userRepository.save(u), User.class);
                            }
                    ).switchIfEmpty(notFound().build());
        });
    }

    public Mono<ServerResponse> findUserById(ServerRequest request) {
        String id = request.pathVariable("id");
        return ok().contentType(APPLICATION_JSON_UTF8)
                .body(userRepository.findById(id), User.class)
                .switchIfEmpty(notFound().build());
    }

    public Mono<ServerResponse> findUserByAge(ServerRequest request) {
        int start = Integer.parseInt(request.pathVariable("start"));
        int end = Integer.parseInt(request.pathVariable("end"));
        return ok().contentType(APPLICATION_JSON_UTF8)
                .body(userRepository.findByAgeBetween(start, end), User.class);
    }

    public Mono<ServerResponse> rangeUser(ServerRequest request) {
        return ok().contentType(APPLICATION_JSON_UTF8)
                .body(userRepository.findUserAgeBetween20To30(), User.class);
    }
}
