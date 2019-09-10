package cn.utokato.webflux01;

import cn.utokato.webflux01.domain.User;
import cn.utokato.webflux01.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * 开发 WebFlux 的基本操作：
 * 1. 安装 MongoDB 数据库
 * -    使用 Docker 直接安装
 * 2. 引入MongoDB数据库依赖
 * -    spring-boot-starter-data-mongodb-reactive
 * 3. 开启数据库相关的注解
 * -    {@link EnableReactiveMongoRepositories}
 * 4. 定义对象
 * -    {@link User}
 * 5. 使用 JPA 操作 MongoDB
 * -    {@link UserRepository}
 * 6. 数据库的配置
 * -    见 application.yml
 * 7. CRUD 编程，详见代码
 *
 * @author lma
 * @date 2019/09/03
 */
@SpringBootApplication
@EnableReactiveMongoRepositories
public class Webflux01Application {

    public static void main(String[] args) {
        SpringApplication.run(Webflux01Application.class, args);
    }

}
