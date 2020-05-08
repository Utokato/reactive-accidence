package cn.utokato.stock.ui;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lma
 * @date 2020/03/15
 */
@SpringBootApplication
public class StockUiApplication {

    public static void main(String[] args) {
        Application.launch(ChartApplication.class, args);
    }

}
