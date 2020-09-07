package cn.utokato.rsocket.demo1;

import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author lma
 * @date 2020/03/16
 */
public class RequestStreamExample {
    public static void main(String[] args) {

        /**
         * 构建服务端，工作模式为 Request-Stream
         * 使用 TCP 协议作为传输层的实现
         * 业务逻辑：将传入的字符串，切割为字符后返回
         */
        RSocketFactory
                .receive()
                .acceptor(
                        ((setup, sendingSocket) -> Mono.just(new AbstractRSocket() {
                            @Override
                            public Flux<Payload> requestStream(Payload payload) {
                                return Flux.fromStream(payload.getDataUtf8()
                                        .codePoints()
                                        .mapToObj(c -> String.valueOf((char) c))
                                        .map(DefaultPayload::create));
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
                // 指定传输层实现
                .transport(TcpClientTransport.create("localhost", 7000))
                //启动客户端
                .start()
                .block();

        assert socket != null;

        /**
         * RSocket 对象用来与服务器端进行交互。
         * RSocket 类的 requestStream() 方法发送 Payload 接口表示的负载并等待响应。
         * 该方法的返回值是表示响应的 Flux<Payload> 对象。对于返回的响应，示例中只是简单地输出到控制台。
         * DefaultPayload.create() 方法可以简单地创建 Payload 对象。
         */
        socket.requestStream(DefaultPayload.create("hello"))
                .map(Payload::getDataUtf8)
                .doOnNext(System.out::println)
                .blockLast();

        socket.dispose();
    }
}
