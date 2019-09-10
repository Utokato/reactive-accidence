package cn.utokato.reactor;

import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 通常情况下，我们需要对源发布者发出的原始数据流进行多个阶段的处理，并最终得到我们需要的数据。
 * 这种感觉就像是一条流水线，从流水线的源头进入传送带的是原料，经过流水线上各个工位的处理
 * 逐渐由原料变成半成品、零件、组件、成品，最终成为消费者需要的包装品。
 * 这其中，流水线源头的下料机就相当于源发布者，消费者就相当于订阅者
 * 流水线上的一道道工序就相当于一个一个的操作符（Operator）
 * <p>
 * 这些操作都有种似曾相识的感觉，没错就是 Java8 中的 stream 中的操作符
 * 再次提出，reactor 是什么 ?
 * reactor = Java8 stream + Java9 flow
 *
 * @author lma
 * @date 2019/09/10
 */
public class TestReactorOperator {


    /**
     * {@link Flux#map(Function)}
     * map操作可以将数据元素进行转换/映射，得到一个新元素
     */
    @Test
    public void testMap() {
        // 将 int 映射为 String
        Flux.just(1, 2, 3, 4, 5, 6).map(String::valueOf).subscribe(System.out::println);
    }

    /**
     * {@link Flux#flatMap(Function)}
     * flatMap操作可以将每个数据元素转换/映射为一个流，然后将这些流合并为一个大的数据流。
     */
    @Test
    public void testFlatMap() {
        // 将每个 String 分割为 char 流，然后将所有的 char 流进行合并
        Flux.just("hello", "reactor").flatMap(str -> Flux.just(str.split(""))).subscribe(System.out::println);
    }

    /**
     * {@link Flux#filter(Predicate)}
     * filter操作可以对数据元素进行筛选
     */
    @Test
    public void testFilter() {
        List<Integer> list = Flux.range(1, 10).filter(i -> i % 2 == 0)
                .toStream().collect(Collectors.toList());
        System.out.println(list);
    }

    /**
     * {@link Flux#zip(Publisher, Publisher)}
     * 看到zip这个词可能会联想到拉链，它能够将多个流一对一的合并起来。zip有多个方法变体，常见的是二合一
     */
    @Test
    public void testZip() {
        // 如果某一个流的数据量较少，最终会以少数为准
        // 如第一个流有7个元素，后一个流只有6个元素，最终结果为只有6个元组
        // [1,a][2,b][3,c][4,d][5,e][6,f]
        Flux.zip(Flux.just(1, 2, 3, 4, 5, 6, 7), Flux.just("a", "b", "c", "d", "e", "f")).subscribe(System.out::print);
    }

    /**
     * {@link Flux#zipWith(Publisher)}
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
     * {@link Flux#take(long)}
     * <p>
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
    public void testFlatMapSuper() {
        Flux.just(1, 2)
                .flatMap(x -> Flux.just("a" + x, "b" + x, "c" + x))
                .toStream()
                .forEach(System.out::println);
    }

    /**
     * concatMap 操作符的作用也是把流中的每个元素转换成一个流，再把所有流进行合并。
     * 与 flatMap 不同的是，concatMap 会根据原始流中的元素顺序依次把转换之后的流进行合并；
     * 与 flatMapSequential 不同的是，concatMap 对转换之后的流的订阅是动态进行的，而 flatMapSequential 在合并之前就已经订阅了所有的流
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
        Flux<List<Integer>> flux = Flux.range(1, 100).buffer(20);
        flux.subscribe(System.out::println);

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
     * {@link Flux#buffer()}
     * 返回值是一个 Flux<List<T>>，即将元素进行了分组收集
     * <p>
     * {@link Flux#window(int)}
     * 返回值是一个 Flux<Flux<T>>，即将元素进行了分流，将一个数据流分成了多个数据流
     */
    @Test
    public void testBufferVsWindow() {
        Flux<List<Long>> buffer = Flux.interval(Duration.ofMillis(100)).buffer(Duration.ofMillis(2001));
        System.out.println("===========");
        Flux<Flux<Long>> window = Flux.interval(Duration.ofMillis(100)).window(Duration.ofMillis(2001));
    }

    /**
     *
     */
    @Test
    public void testTransform() {
        Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                .doOnNext(System.out::println)
                .transform(f -> f.filter(color -> !"orange".equals(color)).map(String::toUpperCase))
                .subscribe(d -> System.out.println("Subscriber to Transformed MapAndFilter: " + d));
    }

    /**
     *
     */
    @Test
    public void testCompose() {
        AtomicInteger atomicInteger = new AtomicInteger();
        Flux<String> composedFlux =
                Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                        .doOnNext(System.out::println)
                        // 注意 compose 与 transform 的区别
                        .compose(f -> {
                                    if (atomicInteger.incrementAndGet() == 1) {
                                        return f.filter(color -> !"orange".equals(color))
                                                .map(String::toUpperCase);
                                    } else {
                                        return f.filter(color -> !"purple".equals(color))
                                                .map(String::toUpperCase);
                                    }
                                }
                        );

        composedFlux.subscribe(d -> System.out.println("Subscriber 1 to Composed MapAndFilter :" + d));
        System.out.println("====");
        composedFlux.subscribe(d -> System.out.println("Subscriber 2 to Composed MapAndFilter: " + d));
    }


    /**
     * 冷热序列
     * <p>
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
