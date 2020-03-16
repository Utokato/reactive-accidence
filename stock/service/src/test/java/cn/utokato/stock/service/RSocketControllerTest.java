package cn.utokato.stock.service;

import cn.utokato.stock.service.model.StockPrice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * @author lma
 * @date 2020/03/16
 */
@SpringBootTest
class RSocketControllerTest extends AbstractTest {


    @Test
    @DisplayName("Test stock by rsocket")
    void testRSocket() {
        // given
        RSocketRequester requester = createRSocketRequester();

        // when
        Flux<StockPrice> prices = requester
                .route("stockPrices")
                .data("SYMBOL")
                .retrieveFlux(StockPrice.class);

        // then
        StepVerifier.create(prices.take(2))
                .expectNextMatches(stockPrice -> stockPrice.getSymbol().equals("SYMBOL"))
                .expectNextMatches(stockPrice -> stockPrice.getSymbol().equals("SYMBOL"))
                .verifyComplete();
    }

}
