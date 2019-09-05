package cn.utokato.webflux03.repository;

import cn.utokato.webflux03.domain.MyEvent;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * @author lma
 * @date 2019/09/05
 */
@Repository
public interface EventRepository extends ReactiveMongoRepository<MyEvent, Long> {
    @Tailable
    Flux<MyEvent> findBy();
}
