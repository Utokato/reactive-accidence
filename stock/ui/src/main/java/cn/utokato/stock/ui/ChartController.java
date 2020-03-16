package cn.utokato.stock.ui;

import cn.utokato.stock.client.WebClientStockClient;
import cn.utokato.stock.client.model.StockPrice;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * @author lma
 * @date 2020/03/15
 */
@Component
public class ChartController {
    @FXML
    public LineChart<String, Double> chart;

    private WebClientStockClient webClientStockClient;


    public ChartController(WebClientStockClient webClientStockClient) {
        this.webClientStockClient = webClientStockClient;
    }

    @FXML
    public void initialize() {
        String symbol1 = "SYMBOL1";
        final PriceSubscribe priceSubscribe1 = new PriceSubscribe(symbol1);
        webClientStockClient.pricesFor(symbol1)
                .subscribe(priceSubscribe1);

        String symbol2 = "SYMBOL2";
        final PriceSubscribe priceSubscribe2 = new PriceSubscribe(symbol2);
        webClientStockClient.pricesFor(symbol2)
                .subscribe(priceSubscribe2);

        ObservableList<XYChart.Series<String, Double>> data = FXCollections.observableArrayList();
        data.add(priceSubscribe1.getSeries());
        data.add(priceSubscribe2.getSeries());
        chart.setData(data);
    }


    private static class PriceSubscribe implements Consumer<StockPrice> {
        private final ObservableList<XYChart.Data<String, Double>> seriesData = FXCollections.observableArrayList();
        private final XYChart.Series<String, Double> series;

        PriceSubscribe(String symbol) {
            this.series = new XYChart.Series<>(symbol, seriesData);
        }

        @Override
        public void accept(StockPrice stockPrice) {
            Platform.runLater(() ->
                    seriesData.add(new XYChart.Data<>(String.valueOf(stockPrice.getTime().getSecond()),
                            stockPrice.getPrice())));
        }

        XYChart.Series<String, Double> getSeries() {
            return series;
        }
    }
}
