package cn.utokato.stock.service.controller;

import cn.utokato.stock.service.model.StockPrice;
import cn.utokato.stock.service.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author lma
 * @date 2020/03/14
 */
@RestController
public class StockController {

    private final PriceService priceService;

    @Autowired
    public StockController(PriceService priceService) {
        this.priceService = priceService;
    }

    /**
     * 服务端推送技术 (SSE Server Sent Event)
     * MediaType text/event-stream
     */
    @GetMapping(value = "/stocks/{symbol}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StockPrice> prices(@PathVariable("symbol") String symbol) {
        return priceService.generatePrices(symbol);

    }

}
