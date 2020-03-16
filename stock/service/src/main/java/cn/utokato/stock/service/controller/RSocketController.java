package cn.utokato.stock.service.controller;

import cn.utokato.stock.service.model.StockPrice;
import cn.utokato.stock.service.service.PriceService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

/**
 * @author lma
 * @date 2020/03/15
 */
@Controller
public class RSocketController {

    private final PriceService priceService;

    public RSocketController(PriceService priceService) {
        this.priceService = priceService;
    }

    @MessageMapping("stockPrices")
    public Flux<StockPrice> prices(String symbol) {
        return priceService.generatePrices(symbol);
    }
}
