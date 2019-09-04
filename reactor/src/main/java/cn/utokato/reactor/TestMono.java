package cn.utokato.reactor;

import org.junit.Test;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author lma
 * @date 2019/09/04
 */
public class TestMono {

    @Test
    public void createMono() {
        /**
         * Mono 类中也包含了一些与 Flux 类中相同的静态方法。
         * 这些方法包括 just()，empty()，error()和 never()等。除了这些方法之外，Mono 还有一些独有的静态方法。
         *
         * 1. fromCallable()、fromCompletionStage()、fromFuture()、fromRunnable()和 fromSupplier()：
         * -    分别从 Callable、CompletionStage、CompletableFuture、Runnable 和 Supplier 中创建 Mono。
         * 2. delay(Duration duration)和 delayMillis(long duration)：创建一个 Mono 序列，在指定的延迟时间之后，产生数字 0 作为唯一值。
         * 3. ignoreElements(Publisher<T> source)：创建一个 Mono 序列，忽略作为源的 Publisher 中的所有元素，只产生结束消息。
         * 4. justOrEmpty(data)和 justOrEmpty(T data)：从一个 Optional 对象或可能为 null 的对象中创建 Mono。
         * -    只有 Optional 对象中包含值或对象不为 null 时，Mono 序列才产生对应的元素。
         * 5. create()方法使用 MonoSink 来创建 Mono
         */
        Mono.fromSupplier(() -> "Hello,Mono").subscribe(System.out::println);
        Mono.justOrEmpty(Optional.of("mono")).subscribe(System.out::println);
        Mono.create(monoSink -> {
            monoSink.success("mono sink");
        }).subscribe(System.out::println);
    }
}
