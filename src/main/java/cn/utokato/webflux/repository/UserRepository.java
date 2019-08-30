package cn.utokato.webflux.repository;

import cn.utokato.webflux.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

// @Repository
public interface UserRepository extends ReactiveCrudRepository<User, String> {
    Mono<User> findByUsername(String username);

    Mono<Long> deleteByUsername(String username);
}
