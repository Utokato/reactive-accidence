# 模拟股票实时数据推送
> 来自于 JetBrain 公司的 Demo

> 涉及的技术点：
>
> - Spring WebFlux 的 SSE，服务端推送技术
> - Spring RSocket 利用 RSocket 实现数据的流式推送
>   - RSocket 是一种的新的应用层协议
>   - 可以基于不同的传输层协议进行通信，如 TCP、WebSocket、UDP 等
> - 使用 WebClient 发起 HTTP 连接
> - 使用 RSocketRequester 发起连接，可以基于 TCP，也可以基于 WebSocket
> - JavaFX 融合 Spring Boot，提供了很好的示例，来说明 Java FX 如何优雅的集成 Spring Boot
> - 底层的网络 I/O 都是通过 Netty 来实现

## 项目结构

```
├─stock                        |   根目录
│  └─service                   |   服务端，对外发送实时数据流
│  └─client                    |   客户端，订阅服务端的实时数据
│  └─ui               		   |   基于客户端，使用 Java FX 展示实时数据
```

其中：client 模块仅作为一个中间的依赖层，供 ui 模块依赖。在 client 模块中声明了如何连接到服务端，如何订阅服务端的数据流，以及订阅之后的数据流返回的格式等。

ui 模块中，融合了 JavaFX 和 Spring Boot。

普通的 Spring Boot 项目，都可以通过一个主类的 main 方法进行启动，如该 demo 中的服务端模块，它的入口方法如下所示：

```java
@SpringBootApplication
public class StockServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockServiceApplication.class, args);
    }
}
```

当我们需要将 JavaFX 与 Spring Boot 融合在一起，就不能直接在 main 方法中启动 Spring Boot，如果直接在主类的 main 方法中启动了 Spring Boot，就无法启动 JavaFX，当然可以在 Spring Boot 启动以后再去拉起 JavaFX，但是在这个 Demo 中，JetBrain 提供了更加优雅的方式。ui 模块的入口方法如下所示：

```java
@SpringBootApplication
public class StockUiApplication {
    public static void main(String[] args) {
        Application.launch(ChartApplication.class, args);
    }
}
```

首先，通过 `@SpringBootApplication` 声明这是一个 Spring Boot 的应用，当应用启动时，Spring Boot 会从该类的 main 方法中进入，这点是由于 Spring Boot 官方对于 Launcher 的重写，这里其实很复杂。但是这个 main 方法中，并没有启动 Spring Application，而是启动了 JavaFX。通过 `Application.launch(...)` 可以拉起一个 JavaFX 应用，需要传入一个继承了 `Application` 的类，如：ChartApplication；

```java
public class ChartApplication extends Application {
    private ConfigurableApplicationContext applicationContext;
    
    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(StockUiApplication.class).run();
    }

    @Override
    public void start(Stage stage) {
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }

    static class StageReadyEvent extends ApplicationEvent {
        public StageReadyEvent(Stage stage) {
            super(stage);
        }

        public Stage getStage() {
            return ((Stage) getSource());

        }
    }
}
```

在 `ChartApplication` 启动的过程中，首先会执行 `init()`，在这个方法中，拉起了 Spring Boot ；之后执行 `start(Stage stage)` 方法，在这个方法中，利用刚刚初始化完成的 Spring Context 发布一个自定义事件 `StageReadyEvent`，这个事件会被 Spring 的容器消费。`StageInitializer` 类继承了 `ApplicationListener` 并设置监听 `StageReadyEvent` 事件。当监听到该事件时，会触发 `onApplicationEvent()`，在这个方法中，完成了对 Javafx stage 的初始化，最终调用 stage 的 show 方法，显示图形化界面。





 

