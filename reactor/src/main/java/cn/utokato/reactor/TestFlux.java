package cn.utokato.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author lma
 * @date 2019/09/04
 */
public class TestFlux {

    @Test
    public void createFlux() {
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
        Flux.just("Hello", "World").subscribe(System.out::println);
        Flux.fromArray(new Integer[]{1, 2, 3}).subscribe(System.out::println);
        Flux.empty().subscribe(System.out::println);
        Flux.range(1, 10).subscribe(System.out::println);
        Flux.interval(Duration.of(10, ChronoUnit.SECONDS)).subscribe(System.out::println);
        /**
         * 8. generate()
         * generate()方法通过同步和逐一的方式来产生 Flux 序列。
         * 序列的产生是通过调用所提供的 SynchronousSink 对象的 next()，complete()和 error(Throwable)方法来完成的;
         *
         */
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
        /**
         * 9. create()
         * create()方法与 generate()方法的不同之处在于所使用的是 FluxSink 对象。
         * FluxSink 支持同步和异步的消息产生，并且可以在一次调用中产生多个元素。
         */
        Flux.create(sink -> {
            for (int i = 0; i < 10; i++) {
                sink.next(i);
            }
            sink.complete();
        }).subscribe(System.out::println);
    }

    /**
     * buffer
     * bufferUntil
     * bufferWhile
     * bufferWhen
     * bufferTimout
     * <p>
     * 注意 buffer 中包含 分组收集的含义
     * 返回值总是一个或多个列表
     * <p>
     * Flux<List<T>>
     */
    @Test
    public void testBuffer() {
        // 产生 1-100 元素的flux，然后每20 buffer(缓冲)一次
        // 最终产生5个列表，每个列表中包含了20个元素
        Flux.range(1, 100).buffer(20).subscribe(System.out::println);

        // 下面这行的意思：每隔100ms产生一个数据，buffer缓冲(等待)2000ms，所以缓冲一次会产生20个数据
        // take = 3 ，代表了前面的动作执行3次，最终产生3个列表，每个列表中包含了20个元素
        Flux.interval(Duration.ofMillis(100)).buffer(Duration.ofMillis(2001)).take(3).toStream().forEach(System.out::println);

        // bufferUntil 直到达到某一条件，就进行buffer(缓存)
        // 产生一个从 1-10 的flux，当元素满足i % 3 == 0时，将通过的这几个元素缓存到一个列表中
        // 最终会形成 [1, 2, 3]，[4, 5, 6]，[7, 8, 9]，[10] 的形式
        Flux.range(1, 10).bufferUntil(i -> i % 3 == 0).subscribe(System.out::println);

        // bufferWhile 当满足某一条件，才进行buffer(缓存)
        // 产生一个从 1-10 的flux，当元素满足i % 3 == 0时，仅将这一个元素进行缓存到一个列表中
        // 最终形成 [3]，[6]，[9] 的形式
        Flux.range(1, 10).bufferWhile(i -> i % 3 == 0).subscribe(System.out::println);

    }


    /**
     * Flux<T>
     */
    @Test
    public void testFilter() {
        Flux.range(1, 10).filter(i -> i % 2 == 0).subscribe(System.out::println);
    }

    /**
     * Flux<Flux<T>>
     */
    @Test
    public void testWindow() {
        // Flux.range(1, 100).window(20).subscribe(System.out::println);

        Flux.interval(Duration.ofMillis(100))
                .window(Duration.ofMillis(1001))
                .take(2)
                .subscribe(System.out::println);
    }

    /**
     * zipWith 操作符把当前流中的元素与另外一个流中的元素按照一对一的方式进行合并。
     * 在合并时可以不做任何处理，由此得到的是一个元素类型为 Tuple2 的流；
     * 也可以通过一个 BiFunction 函数对合并的元素进行处理，所得到的流的元素类型为该函数的返回值。
     */
    @Test
    public void testZipWith() {
        // [a,1]  [b,2]
        Flux.just("a", "b").zipWith(Flux.just("1", "2")).subscribe(System.out::println);
        // a->1   b->2
        Flux.just("a", "b").zipWith(Flux.just("1", "2"), (s1, s2) -> s1 + "->" + s2).subscribe(System.out::println);
    }

    /**
     * take(long n)，take(Duration timespan)：按照指定的数量或时间间隔来提取。
     * takeLast(long n)：提取流中的最后 N 个元素。
     * takeUntil(Predicate predicate)：提取元素直到 Predicate 返回 true。
     * takeWhile(Predicate continuePredicate)： 当 Predicate 返回 true 时才进行提取。
     * takeUntilOther(Publisher other)：提取元素直到另外一个流开始产生元素。
     */
    @Test
    public void testTake() {
        // Flux.range(1, 1000).take(10).subscribe(System.out::println);
        // Flux.range(1, 1000).takeLast(10).subscribe(System.out::println);
        // Flux.range(1, 1000).takeWhile(i -> i < 10).subscribe(System.out::println);
        Flux.range(1, 100).takeUntil(i -> i == 10).subscribe(System.out::println);
    }

    /**
     * reduce 和 reduceWith 操作符对流中包含的所有元素进行累积操作，得到一个包含计算结果的 Mono 序列。
     * 累积操作是通过一个 BiFunction 来表示的。在操作时可以指定一个初始值。如果没有初始值，则序列的第一个元素作为初始值。
     */
    @Test
    public void testReduce() {
        Flux.range(1, 100).reduce((x, y) -> x + y).subscribe(System.out::println);
        Flux.range(1, 100).reduceWith(() -> 100, (x, y) -> x + y).subscribe(System.out::println);
    }


    /**
     * merge 和 mergeSequential 操作符用来把多个流合并成一个 Flux 序列。
     * 不同之处在于 merge 按照所有流中元素的实际产生顺序来合并，而 mergeSequential 则按照所有流被订阅的顺序，以流为单位进行合并。
     */
    @Test
    public void testMerge() {
        Flux.merge(Flux.interval(Duration.ofMillis(100)).take(5), Flux.interval(Duration.ofMillis(100)).take(10))
                .toStream().forEach(System.out::println);

        System.out.println("================");

        Flux.mergeSequential(
                Flux.interval(Duration.ofMillis(100), Duration.ofMillis(10)).take(5),
                Flux.interval(Duration.ofMillis(100), Duration.ofMillis(50)).take(10)
        ).toStream().forEach(System.out::println);
    }

    /**
     * flatMap 和 flatMapSequential 操作符把流中的每个元素转换成一个流，再把所有流中的元素进行合并。
     * flatMapSequential 和 flatMap 之间的区别与 mergeSequential 和 merge 之间的区别是一样的。
     */
    @Test
    public void testFlatMap() {
        Flux.just(1, 2)
                .flatMap(x -> Flux.just("a" + x, "b" + x, "c" + x))
                .toStream()
                .forEach(System.out::println);
    }

    /**
     * concatMap 操作符的作用也是把流中的每个元素转换成一个流，再把所有流进行合并。
     * 与 flatMap 不同的是，concatMap 会根据原始流中的元素顺序依次把转换之后的流进行合并；
     * 与 flatMapSequential 不同的是，concatMap 对转换之后的流的订阅是动态进行的，而 flatMapSequential 在合并之前就已经订阅了所有的流。
     */
    @Test
    public void testConcatMap() {
        Flux.just(1, 2)
                .concatMap(x -> Flux.just("a" + x, "b" + x, "c" + x))
                .toStream()
                .forEach(System.out::println);
    }

    /**
     * combineLatest 操作符把所有流中的最新产生的元素合并成一个新的元素，作为返回结果流中的元素。
     * 只要其中任何一个流中产生了新的元素，合并操作就会被执行一次，结果流中就会产生新的元素。
     */
    @Test
    public void testCombineLatest() {
        Flux.combineLatest(Arrays::toString, Flux.just(0),
                Flux.just("a", "b", "c"),
                Flux.interval(Duration.ofMillis(100), Duration.ofMillis(50)).take(5))
                .toStream().forEach(System.out::println);
    }


    // ---- 订阅消息 ： 消息处理

    /**
     * 当需要处理 Flux 或 Mono 中的消息时，如之前的代码清单所示，可以通过 subscribe 方法来添加相应的订阅逻辑。
     * 在调用 subscribe 方法时可以指定需要处理的消息类型。可以只处理其中包含的正常消息，也可以同时处理错误消息和完成消息。
     */
    @Test
    public void testSubscribe() {
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .subscribe(System.out::println, System.err::println);

        /**
         * 正常的消息处理相对简单。当出现错误时，有多种不同的处理策略。第一种策略是通过 onErrorReturn()方法返回一个默认值
         */
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .onErrorReturn(Integer.MIN_VALUE)
                .subscribe(System.out::println);

        /**
         * 第二种策略是通过 onErrorResume()方法来使用另外的流来产生元素
         */
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .onErrorResume(e -> Flux.just(9, 10, 11))
                .subscribe(System.out::println);

        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalArgumentException()))
                .onErrorResume(e -> {
                    if (e instanceof IllegalStateException) {
                        return Mono.just(0);
                    } else if (e instanceof IllegalArgumentException) {
                        return Mono.just(-1);
                    }
                    return Mono.empty();
                })
                .subscribe(System.out::println);

        System.out.println("===============");

        /**
         * 当出现错误时，还可以通过 retry 操作符来进行重试。重试的动作是通过重新订阅序列来实现的。
         * 再使用 retry 操作符时可以指定重试的次数。
         */
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .retry(3)
                .subscribe(System.out::println, System.err::println);
    }

    // ---- 调度器 Schedulers

    /**
     * 通过调度器（Scheduler）可以指定这些操作执行的方式和所在的线程
     * <p>
     * 1. 当前线程，通过 Schedulers.immediate()方法来创建。
     * 2. 单一的可复用的线程，通过 Schedulers.single()方法来创建。
     * 3. 使用弹性的线程池，通过 Schedulers.elastic()方法来创建。线程池中的线程是可以复用的。当所需要时，新的线程会被创建。
     * -    如果一个线程闲置太长时间，则会被销毁。该调度器适用于 I/O 操作相关的流的处理。
     * 4. 使用对并行操作优化的线程池，通过 Schedulers.parallel()方法来创建。
     * -    其中的线程数量取决于 CPU 的核的数量。该调度器适用于计算密集型的流的处理。
     * 5. 使用支持任务调度的调度器，通过 Schedulers.timer()方法来创建。
     * 6. 从已有的 ExecutorService 对象中创建调度器，通过 Schedulers.fromExecutorService()方法来创建。
     * <p>
     * 某些操作符默认就已经使用了特定类型的调度器。比如 interval()方法创建的流就使用了由 Schedulers.timer()创建的调度器。
     * <p>
     * 通过 publishOn()和 subscribeOn()方法可以切换执行操作的调度器。
     * 其中 publishOn()方法切换的是操作符的执行方式，而 subscribeOn()方法切换的是产生流中元素时的执行方式。
     */
    @Test
    public void testSchedulers() {
        Flux.create(sink -> {
            sink.next(Thread.currentThread().getName());
            sink.complete();
        }).publishOn(Schedulers.single())
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                .publishOn(Schedulers.elastic())
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                .subscribeOn(Schedulers.parallel())
                .toStream()
                .forEach(System.out::println);
    }


    // -- 冷热序列

    /**
     * 之前的代码清单中所创建的都是冷序列。冷序列的含义是不论订阅者在何时订阅该序列，总是能收到序列中产生的全部消息。
     * 而与之对应的热序列，则是在持续不断地产生消息，订阅者只能获取到在其订阅之后产生的消息。
     */
    @Test
    public void test() throws InterruptedException {
        final Flux<Long> source = Flux.interval(Duration.ofMillis(1000))
                .take(10)
                .publish() // 把一个 Flux 对象转换成 ConnectableFlux 对象
                .autoConnect(); // 当 ConnectableFlux 对象有一个订阅者时就开始产生消息

        source.subscribe(); // 订阅该 ConnectableFlux 对象，使其开始产生数据
        TimeUnit.SECONDS.sleep(5); // 线程休眠 5s
        source.toStream().forEach(System.out::println); // 订阅者此时只能获得到该序列中的后 5 个元素
    }
}
