package cn.utokato.webflux01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
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
