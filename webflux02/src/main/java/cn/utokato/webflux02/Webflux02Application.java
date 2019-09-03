package cn.utokato.webflux02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
@SpringBootApplication
public class Webflux02Application {

    public static void main(String[] args) {
        SpringApplication.run(Webflux02Application.class, args);
    }

}
