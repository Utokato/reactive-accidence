package cn.utokato.rsocket.demo1;

import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author lma
 * @date 2020/03/16
 */
public class RequestChannelExample {

    public static void main(String[] args) {
        /**
         * 构建服务端，工作模式为 Request-Channel
         * 使用 TCP 协议作为传输层的实现
         */
        RSocketFactory
                .receive()
                .acceptor(
                        ((setup, sendingSocket) -> Mono.just(new AbstractRSocket() {
                            @Override
                            public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
                                return Flux.from(payloads)
                                        .flatMap(payload -> Flux.fromStream(payload.getDataUtf8()
                                                .codePoints()
                                                .mapToObj(c -> String.valueOf((char) c))
                                                .map(DefaultPayload::create)));
                            }
                        })))
                // 指定传输层实现
                .transport(TcpServerTransport.create("localhost", 7000))
                // 启动服务器
                .start()
                .subscribe();

        /**
         * 构建客户端，通过客户端传输协议连接到制定的 IP 和 PORT 上
         * 启动客户端，调用 block() 方法等待客户端启动并返回 RSocket 对象
         */
        RSocket socket = RSocketFactory.connect()
                .transport(TcpClientTransport.create("localhost", 7000))
                .start()
                .block();

        assert socket != null;

        /**
         * 客户端 RSocket 对象使用 requestChannel() 方法发送请求并处理响应。
         */
        socket.requestChannel(Flux.just("hello", "world", "goodbye")
                .map(DefaultPayload::create))
                .map(Payload::getDataUtf8)
                .doOnNext(System.out::println)
                .blockLast();

        socket.dispose();
    }
}
