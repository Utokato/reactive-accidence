package cn.utokato.webflux03;

import cn.utokato.webflux03.controller.EventController;
import cn.utokato.webflux03.domain.MyEvent;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * 一个异步非阻塞响应式的demo
 * <p>
 * 模型为一个简单的 event，{@link MyEvent}
 * <p>
 * 在{@link EventController}中定义了两个方法，一个post用来创建数据，一个get用来获取消费数据
 * 在 Webflux03ApplicationTests 中使用 {@link WebClient} 写了一个客户端
 * 通过该客户端发送post请求来创建数据
 * <p>
 * 之后再浏览器中通过get请求进行访问，就能看到不断创建地数据流向了mongodb中
 * 又由服务端推送到了浏览器中，实现了一个流式管道地搭建
 *
 * @author lma
 * @date 2019/09/05
 */
@SpringBootApplication
@EnableReactiveMongoRepositories
public class Webflux03Application {

    public static void main(String[] args) {
        SpringApplication.run(Webflux03Application.class, args);
    }

    @Bean
    public CommandLineRunner initData(MongoOperations mongoOperations) {
        return (String... args) -> {
            mongoOperations.dropCollection(MyEvent.class);
            mongoOperations.createCollection(MyEvent.class,
                    CollectionOptions.empty().maxDocuments(200).size(100000).capped());
        };
    }

}
