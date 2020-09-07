package cn.utokato.rsocket.demo1.websocket;

import cn.utokato.rsocket.demo1.RequestResponseExample;
import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.WebsocketClientTransport;
import io.rsocket.transport.netty.server.WebsocketServerTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Mono;

/**
 * WebSocket 是一种在单个 TCP 连接上进行全双工通信的<b>协议</b>
 *
 * Dont like {@link RequestResponseExample} that was built in tcp protocol,
 * this demo is built with web socket.
 *
 * @author lma
 * @date 2020/03/16
 * @see RequestResponseExample
 */
public class WebSocketRequestResponseExample {
    public static void main(String[] args) {
        /**
         * 构建服务端，工作模式为 Request-Response
         * 使用 Websocket 协议作为传输层的实现
         */
        RSocketFactory.receive()
                .acceptor(((setup, sendingSocket) -> Mono.just(
                        new AbstractRSocket() {
                            @Override
                            public Mono<Payload> requestResponse(Payload payload) {
                                return Mono.just(DefaultPayload.create("ECHO >> " + payload.getDataUtf8()));
                            }
                        }
                )))
                .transport(WebsocketServerTransport.create("localhost", 7000))
                .start()
                .subscribe();

        /**
         * 构建客户端，通过客户端传输协议连接到制定的 IP 和 PORT 上
         * 启动客户端，调用 block() 方法等待客户端启动并返回 RSocket 对象
         */
        RSocket socket = RSocketFactory.connect()
                .transport(WebsocketClientTransport.create("localhost", 7000))
                .start()
                .block();

        assert socket != null;

        socket.requestResponse(DefaultPayload.create("hello"))
                .map(Payload::getDataUtf8)
                .doOnNext(System.out::println)
                .block();

        socket.dispose();

    }
}
