package cn.utokato.stock.client;


import cn.utokato.stock.client.model.StockPrice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = TestApplication.class)
public class RSocketStockClientIntegrationTest {

    private RSocketRequester createRSocketRequester() {
        return RSocketRequester.builder()
                .connectTcp("127.0.0.1", 7070).block();
    }

    @Test
    public void shouldRetrievesStockPricesFromTheService() {
        // given
        RSocketStockClient rSocketStockClient = new RSocketStockClient(createRSocketRequester());

        // when
        Flux<StockPrice> prices = rSocketStockClient.pricesFor("SYMBOL");

        /* assertNotNull(prices);
        Flux<StockPrice> fivePrices = prices.take(5);
        assertEquals(5, fivePrices.count().block());
        assertEquals("SYMBOL", Objects.requireNonNull(fivePrices.blockFirst()).getSymbol());*/

        // then
        StepVerifier.create(prices.take(2))
                .expectNextMatches(stockPrice -> stockPrice.getSymbol().equals("SYMBOL"))
                .expectNextMatches(stockPrice -> stockPrice.getSymbol().equals("SYMBOL"))
                .verifyComplete();
    }

}