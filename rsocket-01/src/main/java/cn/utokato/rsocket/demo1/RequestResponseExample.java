package cn.utokato.rsocket.demo1;

import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Mono;

/**
 * @author lma
 * @date 2020/03/16
 */
public class RequestResponseExample {

    public static void main(String[] args) {
        /**
         * 构建服务端，工作模式为 Request-Response
         * 使用 TCP 协议作为传输层的实现
         * 要意识到 RSocket (Reactive Socket) 本质上是应用层的协议，隶属于 5/6 层
         * 网络传输，需要依赖于 4 层的传输协议，如 TCP 、WebSocket 等
         * {@link ServerTransport} 服务端的传输接口，查看这个接口的子类
         * 可以发现目前 RSocket 使用的就是 TCP 和 WebSocket 两种通信协议
         */
        RSocketFactory
                .receive()
                // 服务端 使用 负载零拷贝
                /**
                 * 示例代码中，对于接收的 Payload 对象是直接使用的。
                 * 这是因为 RSocket 默认对请求的负载进行了拷贝。
                 * 这样的做法在实现上虽然简单，但会带来性能上的损失，增加响应时间。
                 * 为了提高性能，可以通过 ServerRSocketFactory 类或 ClientRSocketFactory 类的
                 * -        frameDecoder() 方法来指定 PayloadDecoder 接口的实现。
                 * PayloadDecoder.ZERO_COPY 是内置提供的零拷贝实现类。当使用了负载零拷贝之后，负载的内容不再被拷贝。
                 * 需要通过 Payload 对象的 release() 方法来手动释放负载对应的内存，否则会造成内存泄漏。
                 * 如果使用 Spring Boot 提供的 RSocket 支持，PayloadDecoder.ZERO_COPY 默认已经被启用，
                 * 并由 Spring 负责相应的内存释放。
                 */
                .frameDecoder(PayloadDecoder.ZERO_COPY)
                .acceptor(
                        ((setup, sendingSocket) -> Mono.just(new AbstractRSocket() {
                            @Override
                            public Mono<Payload> requestResponse(Payload payload) {
                                return Mono.just(DefaultPayload.create("ECHO >> " + payload.getDataUtf8()));
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
        RSocket socket = RSocketFactory
                .connect()
                // 客户端 使用 负载零拷贝
                .frameDecoder(PayloadDecoder.ZERO_COPY)
                // 指定传输层实现
                .transport(TcpClientTransport.create("localhost", 7000))
                //启动客户端
                .start()
                .block();

        assert socket != null;


        /**
         * RSocket 对象用来与服务器端进行交互。
         * RSocket 类的 requestResponse() 方法发送 Payload 接口表示的负载并等待响应。
         * 该方法的返回值是表示响应的 Mono<Payload> 对象。对于返回的响应，示例中只是简单地输出到控制台。
         * DefaultPayload.create() 方法可以简单地创建 Payload 对象。
         */
        socket.requestResponse(DefaultPayload.create("hello"))
                .map(Payload::getDataUtf8)
                .doOnNext(System.out::println)
                .block();

        /**
         * RSocket 类的 dispose() 方法用来销毁该对象。
         */
        socket.dispose();
    }
}
