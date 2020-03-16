package cn.utokato.stock.client;

import cn.utokato.stock.client.model.StockPrice;
import reactor.core.publisher.Flux;

/**
 * @author lma
 * @date 2020/03/15
 */
public interface StockClient {
    Flux<StockPrice> pricesFor(String symbol);
}
