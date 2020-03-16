package cn.utokato.stock.client;


import cn.utokato.stock.client.model.StockPrice;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class WebClientStockClientIntegrationTest {

    private WebClient webClient = WebClient.builder().build();

    @Test
    void shouldRetrievesStockPricesFromTheService() {
        WebClientStockClient webClientStockClient = new WebClientStockClient(webClient);
        Flux<StockPrice> prices = webClientStockClient.pricesFor("SYMBOL");

        assertNotNull(prices);
        Flux<StockPrice> fivePrices = prices.take(5);
        assertEquals(5, fivePrices.count().block());
        assertEquals("SYMBOL", Objects.requireNonNull(fivePrices.blockFirst()).getSymbol());
    }

}