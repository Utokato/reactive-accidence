package cn.utokato.stock.service;

import io.rsocket.transport.netty.client.TcpClientTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.MimeTypeUtils;

/**
 * @author lma
 * @date 2020/03/16
 */
abstract class AbstractTest {

    @Value("${spring.rsocket.server.port}")
    private int serverPort;
    @Autowired
    private RSocketRequester.Builder builder;

    RSocketRequester createRSocketRequester() {
        return builder
                // 注意Flux<T> 的 T 是对象类型是，指定 APPLICATION_JSON
                // 表明数据 codec 方式使用 JSON
                // 基本数据类型，使用 TEXT_PLAIN
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .connect(TcpClientTransport.create(serverPort))
                .block();
    }
}
