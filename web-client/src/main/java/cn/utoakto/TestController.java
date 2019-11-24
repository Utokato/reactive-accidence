package cn.utoakto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author lma
 * @date 2019/11/24
 */
@RestController
public class TestController {

    @Autowired
    IUserApi userApi;

    @GetMapping
    public void getAll() {
        // 测试信息的提取
        // 不订阅，不会实际发出请求，但会进入自定义的代理类
        // userApi.getAllUser();
        // userApi.getUserById("123456");
        // userApi.deleteUserById("123456");
        // userApi.createUser(Mono.just(User.builder().name("123").build()));


        // 直接调用，实现调用rest接口的效果
        // Flux<User> userFlux = userApi.getAllUser();
        // userFlux.subscribe(System.out::println);

        // String id = "5dda4058e90bff0af8953df8";
        // userApi.getUserById(id).subscribe(System.out::println, System.err::println);
        // id = "5dda4058e90bff0af8953df7";
        // userApi.getUserById(id).subscribe(System.out::println, System.err::println);

        userApi.createUser(Mono.just(User.builder().name("malong").age(26).build())).subscribe(System.out::println);
    }
}
