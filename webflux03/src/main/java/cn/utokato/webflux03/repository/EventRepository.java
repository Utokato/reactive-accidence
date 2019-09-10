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

    /**
     * 获取 mongodb 中最新的数据
     *
     * @return
     */
    @Tailable
    Flux<MyEvent> findBy();
}
