package cn.utokato.rsocket.config;

import io.rsocket.RSocketFactory;
import io.rsocket.SocketAcceptor;
import io.rsocket.transport.ServerTransport;
import io.rsocket.transport.netty.server.WebsocketRouteTransport;
import org.springframework.boot.web.embedded.netty.NettyRouteProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.netty.http.server.HttpServerRoutes;

/**
 * @author lma
 * @date 2020/03/16
 */
@Configuration
@EnableWebFlux
public class AppConfig {

    @Bean
    RSocketWebSocketNettyRouteProvider rSocketWebsocketRouteProvider(
            RSocketMessageHandler messageHandler) {
        return new RSocketWebSocketNettyRouteProvider("/ws",
                messageHandler.responder());
    }

    static class RSocketWebSocketNettyRouteProvider implements NettyRouteProvider {

        private final String mappingPath;

        private final SocketAcceptor socketAcceptor;

        RSocketWebSocketNettyRouteProvider(String mappingPath, SocketAcceptor socketAcceptor) {
            this.mappingPath = mappingPath;
            this.socketAcceptor = socketAcceptor;
        }

        @Override
        public HttpServerRoutes apply(HttpServerRoutes httpServerRoutes) {
            ServerTransport.ConnectionAcceptor acceptor = RSocketFactory.receive()
                    .acceptor(this.socketAcceptor)
                    .toConnectionAcceptor();
            return httpServerRoutes.ws(this.mappingPath, WebsocketRouteTransport.newHandler(acceptor));
        }

    }

}
