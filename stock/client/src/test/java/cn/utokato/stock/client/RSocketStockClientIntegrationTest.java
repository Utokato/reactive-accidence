package cn.utokato.stock.client;


import cn.utokato.stock.client.model.StockPrice;
import io.rsocket.transport.netty.client.TcpClientTransport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class RSocketStockClientIntegrationTest {

    @Autowired
    private RSocketRequester.Builder builder;

    RSocketRequester createRSocketRequester() {
        return builder
                // 注意Flux<T> 的 T 是对象类型是，指定 APPLICATION_JSON
                // 表明数据 codec 方式使用 JSON
                // 基本数据类型，使用 TEXT_PLAIN
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .connect(TcpClientTransport.create(7070))
                .block();
    }

    @Test
    void shouldRetrievesStockPricesFromTheService() {
        // given
        RSocketStockClient rSocketStockClient = new RSocketStockClient(createRSocketRequester());

        // when
        Flux<StockPrice> prices = rSocketStockClient.pricesFor("SYMBOL");

        // then
        StepVerifier.create(prices.take(2))
                .expectNextMatches(stockPrice -> stockPrice.getSymbol().equals("SYMBOL"))
                .expectNextMatches(stockPrice -> stockPrice.getSymbol().equals("SYMBOL"))
                .verifyComplete();
    }

}