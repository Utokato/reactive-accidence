package cn.utokato.stock.client;

import io.rsocket.transport.netty.client.TcpClientTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author lma
 * @date 2020/03/15
 */
@Configuration
public class ClientConfiguration {

    @Bean
    @Profile("sse")
    public StockClient webClientStockClient(WebClient webClient) {
        return new WebClientStockClient(webClient);
    }

    @Bean
    @Profile("rsocket")
    public StockClient rSocketStockClient(RSocketRequester rSocketRequester) {
        return new RSocketStockClient(rSocketRequester);
    }

    @Bean
    public RSocketRequester rSocketRequester(RSocketRequester.Builder builder) {
        return builder
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .connect(TcpClientTransport.create(7070))
                .block();
    }


    @Bean
    @ConditionalOnMissingBean
    public WebClient webClient() {
        return WebClient.builder().build();
    }


}
