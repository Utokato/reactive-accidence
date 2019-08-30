package cn.utokato.webflux;

import cn.utokato.webflux.model.IEvent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;

/**
 * This is a demo project learning from CSDN blog, and the site is :
 * https://blog.csdn.net/column/details/20300.html
 *
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner initData(MongoOperations mongo) {
        return (String... args) -> {
            mongo.dropCollection(IEvent.class);
            mongo.createCollection(IEvent.class, CollectionOptions.empty().maxDocuments(200).size(100000).capped());
        };
    }
}
