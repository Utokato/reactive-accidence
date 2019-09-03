package cn.utokato.webflux02.router;

import cn.utokato.webflux02.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


/**
 * @author lma
 * @date 2019/09/03
 */
@Configuration
public class AllRouter {

    @Bean
    RouterFunction<ServerResponse> userRouter(UserHandler handler) {
        return nest(
                path("/user"),
                route(GET("/"), handler::getAll)
                        .andRoute(POST("/").and(accept(APPLICATION_JSON_UTF8)), handler::createUser)
                        .andRoute(DELETE("/{id}"), handler::deleteUser)
                        .andRoute(PUT("/{id}").and(accept(APPLICATION_JSON_UTF8)), handler::updateUser)
                        .andRoute(GET("/{id}"), handler::findUserById)
                        .andRoute(GET("/{start}/{end}"), handler::findUserByAge)
                        .andRoute(GET("/range"), handler::rangeUser)
        );
    }
}
