package cn.utokato.stock.client;

import cn.utokato.stock.client.model.StockPrice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;

/**
 * @author lma
 * @date 2020/03/15
 */
@Slf4j
public class WebClientStockClient implements StockClient {

    private WebClient webClient;

    public WebClientStockClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<StockPrice> pricesFor(String symbol) {
        log.info("WebClient stock client");
        return webClient.get()
                .uri("http://127.0.0.1:8080/stocks/{symbol}", symbol)
                .retrieve()
                .bodyToFlux(StockPrice.class)
                .retryBackoff(5, Duration.ofSeconds(1), Duration.ofSeconds(20))
                .doOnError(IOException.class, e -> log.error(e.getMessage()));
    }
}
