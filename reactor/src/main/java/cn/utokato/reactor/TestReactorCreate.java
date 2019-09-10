package cn.utokato.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

/**
 * Reactor中的发布者（Publisher）由Flux和Mono两个类定义，它们都提供了丰富的操作符（operator）。
 * 一个Flux对象代表一个包含0..N个元素的响应式序列
 * 一个Mono对象代表一个包含零/一个（0..1）元素的结果。
 * <p>
 * 既然是“数据流”的发布者，Flux和Mono都可以发出三种“数据信号”：元素值、错误信号、完成信号
 * 错误信号和完成信号都是终止信号
 * 完成信号用于告知下游订阅者该数据流正常结束
 * 错误信号终止数据流的同时将错误传递给下游订阅者。
 *
 * @author lma
 * @date 2019/09/10
 */
public class TestReactorCreate {


    /**
     * Mono 类中也包含了一些与 Flux 类中相同的静态方法。
     * 这些方法包括 just()，empty()，error()和 never()等。除了这些方法之外，Mono 还有一些独有的静态方法。
     * <p>
     * 1. fromCallable()、fromCompletionStage()、fromFuture()、fromRunnable()和 fromSupplier()：
     * -    分别从 Callable、CompletionStage、CompletableFuture、Runnable 和 Supplier 中创建 Mono。
     * 2. delay(Duration duration)和 delayMillis(long duration)：创建一个 Mono 序列，在指定的延迟时间之后，产生数字 0 作为唯一值。
     * 3. ignoreElements(Publisher<T> source)：创建一个 Mono 序列，忽略作为源的 Publisher 中的所有元素，只产生结束消息。
     * 4. justOrEmpty(data)和 justOrEmpty(T data)：从一个 Optional 对象或可能为 null 的对象中创建 Mono。
     * -    只有 Optional 对象中包含值或对象不为 null 时，Mono 序列才产生对应的元素。
     * 5. create()方法使用 MonoSink 来创建 Mono
     */
    @Test
    public void testCreateMono() {
        Mono.error(new Exception("some error")).subscribe(System.out::println);
        Mono.fromSupplier(() -> "Hello,Mono").subscribe(System.out::println);
        Mono.justOrEmpty(Optional.of("mono")).subscribe(System.out::println);
        Mono.create(monoSink -> {
            monoSink.success("mono sink");
        }).subscribe(System.out::println);
    }

    /**
     * 1. just()：可以指定序列中包含的全部元素。创建出来的 Flux 序列在发布这些元素之后会自动结束。
     * 2. fromArray()，fromIterable()和 fromStream()：可以从一个数组、Iterable 对象或 Stream 对象中创建 Flux 对象。
     * 3. empty()：创建一个不包含任何元素，只发布结束消息的序列。
     * 4. error(Throwable error)：创建一个只包含错误消息的序列。
     * 5. never()：创建一个不包含任何消息通知的序列。
     * 6. range(int start, int count)：创建包含从 start 起始的 count 个数量的 Integer 对象的序列。
     * 7. interval(Duration period)和 interval(Duration delay, Duration period)：创建一个包含了从 0 开始递增的 Long 对象的序列
     * -    其中包含的元素按照指定的间隔来发布。除了间隔时间之外，还可以指定起始元素发布之前的延迟时间。
     */
    @Test
    public void testCreateFlux1() {
        Flux.just("Hello", "World").subscribe(System.out::println);
        Flux.fromArray(new Integer[]{1, 2, 3}).subscribe(System.out::println);
        Flux.empty().subscribe(System.out::println);
        Flux.error(new Exception("some error")).subscribe(System.out::println);
        Flux.range(1, 10).subscribe(System.out::println);
        Flux.interval(Duration.of(10, ChronoUnit.SECONDS)).subscribe(System.out::println);
    }

    /**
     * 8. generate()
     * generate()方法通过同步和逐一的方式来产生 Flux 序列。
     * 序列的产生是通过调用所提供的 SynchronousSink 对象的 next()，complete()和 error(Throwable)方法来完成的;
     */
    @Test
    public void testCreateFlux2() {
        Flux.generate(sink -> {
            sink.next("hello");
            sink.complete();
        }).subscribe(System.out::println);
        final Random random = new Random();
        Flux.generate(ArrayList::new, (list, sink) -> {
            int value = random.nextInt(100);
            list.add(value);
            sink.next(value);
            if (list.size() == 10) {
                sink.complete();
            }
            return list;
        }).subscribe(System.out::println);
    }

    /**
     * 9. create()
     * create()方法与 generate()方法的不同之处在于所使用的是 FluxSink 对象。
     * FluxSink 支持同步和异步的消息产生，并且可以在一次调用中产生多个元素。
     */
    @Test
    public void testCreateFlux3() {
        Flux.create(sink -> {
            for (int i = 0; i < 10; i++) {
                sink.next(i);
            }
            sink.complete();
        }).subscribe(System.out::println);
    }
}
