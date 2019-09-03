package cn.utokato.webflux02.repository;

import cn.utokato.webflux02.domain.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * @author lma
 * @date 2019/09/03
 */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    /**
     * 根据年龄段查找用户
     * <p>
     * findByAgeBetween JPA语法
     *
     * @param start
     * @param end
     * @return
     */
    Flux<User> findByAgeBetween(int start, int end);

    /**
     * 使用MongoDB的查询语句
     * 注意MongoDB的使用
     *
     * @return
     */
    @Query("{'age':{'$gte':20,'$lte':30}}")
    Flux<User> findUserAgeBetween20To30();
}
