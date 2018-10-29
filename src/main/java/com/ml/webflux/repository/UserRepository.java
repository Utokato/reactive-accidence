package com.ml.webflux.repository;

import com.ml.webflux.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

// @Repository
public interface UserRepository extends ReactiveCrudRepository<User,String> {
    Mono<User> findByUsername(String username);
    Mono<Long> deleteByUsername(String username);
}
