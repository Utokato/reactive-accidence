package cn.utokato.webflux01.controller;

import cn.utokato.webflux01.domain.User;
import cn.utokato.webflux01.repository.UserRepository;
import cn.utokato.webflux01.util.CheckUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.Duration;

/**
 * @author lma
 * @date 2019/09/03
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 获取所有的数据，以列表的形式返回
     *
     * @return
     */
    @GetMapping("/all")
    public Flux<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * 获取所有的数据，以数据流的方式返回
     *
     * @return
     */
    @GetMapping(value = "/stream/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamGetAll() {
        return userRepository.findAll().delayElements(Duration.ofSeconds(1));
    }

    /**
     * 新增数据
     *
     * @param user
     * @return
     * @ Valid 进行参数校验
     * @ RequestBody 获取属性，请求以json的形式发送
     */
    @PostMapping("/")
    public Mono<User> createUser(@Valid @RequestBody User user) {
        // spring data jpa 中，新增和修改都是save方法。id不为空是修改，id为空是新增
        // 根据实际情况来判断是否置空id
        user.setId(null);
        CheckUtils.checkName(user.getName());
        return userRepository.save(user);
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     * @ PathVariable 获取请求路径上的参数
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable("id") String id) {
        // 直接使用 JPA 的方式进行删除数据，没有返回值，不知道数据是否删除成功
        // 所以，先查询到该数据再进行删除
        return userRepository.findById(id)
                // 当需要操作数据，并返回一个Mono，这个时候使用flatMap
                // 如果不操作数据，只是对数据进行转换，这时候使用map
                // 注意区别，什么时候使用flatMap，什么时候使用map
                .flatMap(user -> userRepository.delete(user).then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 修改数据
     * 先查询到数据，然后进行修改
     *
     * @param id
     * @param user
     * @return
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<User>> updateUser(@PathVariable("id") String id, @Valid @RequestBody User user) {
        CheckUtils.checkName(user.getName());
        return userRepository.findById(id)
                // flatMap 操作数据，会返回一个Mono对象
                .flatMap(u -> {
                    u.setName(user.getName());
                    u.setAge(user.getAge());
                    return userRepository.save(u);
                })
                // Map 转换数据
                .map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 根据id查询数据
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<User>> findUserById(@PathVariable("id") String id) {
        return userRepository.findById(id)
                .map(u -> new ResponseEntity<>(u, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * 根据年龄段进行查询，以列表的形式返回
     *
     * @param start
     * @param end
     * @return
     */
    @GetMapping("/age/{start}/{end}")
    public Flux<User> findByAge(@PathVariable("start") int start, @PathVariable("end") int end) {
        return userRepository.findByAgeBetween(start, end);
    }

    /**
     * 根据年龄段进行查询，以数据流的形式返回
     *
     * @param start
     * @param end
     * @return
     */
    @GetMapping(value = "/stream/age/{start}/{end}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamFindByAge(@PathVariable("start") int start, @PathVariable("end") int end) {
        return userRepository.findByAgeBetween(start, end);
    }

    /**
     * 查询年龄在20-30之间的数据，以列表的形式返回
     *
     * @return
     */
    @GetMapping("/range")
    public Flux<User> rangeUser() {
        return userRepository.findUserAgeBetween20To30();
    }

    /**
     * 查询年龄在20-30之间的数据，以数据流的形式返回
     *
     * @return
     */
    @GetMapping(value = "/stream/range", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamRangeUser() {
        return userRepository.findUserAgeBetween20To30();
    }
}
