package cn.utokato.stock.service.service;

import cn.utokato.stock.service.model.StockPrice;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lma
 * @date 2020/03/15
 */
@Service
public class PriceService {

    public Flux<StockPrice> generatePrices(String symbol) {
        return Flux.interval(Duration.ofSeconds(1))
                .map(item -> new StockPrice(symbol, randomStockPrice(), LocalDateTime.now()));
    }

    private Double randomStockPrice() {
        return ThreadLocalRandom.current().nextDouble(100.0);
    }
}
