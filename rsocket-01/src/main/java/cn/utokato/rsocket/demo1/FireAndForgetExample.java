package cn.utokato.rsocket.demo1;

import io.rsocket.AbstractRSocket;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * @author lma
 * @date 2020/03/16
 */
public class FireAndForgetExample {
    public static void main(String[] args) throws InterruptedException {

        /**
         * 构建服务端，工作模式为 Fire-And-Forget
         * 使用 TCP 协议作为传输层的实现
         * 在发后不管模式中，由于发送方不需要等待接收方的响应，
         * 因此当程序结束时，服务器端并不一定接收到了请求
         */
        RSocketFactory
                .receive()
                .acceptor(
                        ((setup, sendingSocket) -> Mono.just(new AbstractRSocket() {
                            @Override
                            public Mono<Void> fireAndForget(Payload payload) {
                                System.out.println("Server Receive: " + payload.getDataUtf8());
                                return Mono.empty();
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

        socket.fireAndForget(DefaultPayload.create("hello")).block();
        socket.fireAndForget(DefaultPayload.create("world")).block();

        // 休眠 3 秒，保证服务端可以接受到服务端发送过来的数据
        TimeUnit.SECONDS.sleep(3);

        socket.dispose();
    }
}
