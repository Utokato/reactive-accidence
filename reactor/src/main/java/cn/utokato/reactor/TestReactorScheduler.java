package cn.utokato.reactor;


import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

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
 *
 * @author lma
 * @date 2019/09/10
 */
public class TestReactorScheduler {

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

    @Test
    public void testParallelFlux1() throws InterruptedException {
        Flux.range(1, 10)
                .publishOn(Schedulers.parallel())
                .log().subscribe();
        TimeUnit.MILLISECONDS.sleep(10);
    }

    @Test
    public void testParallelFlux2() throws InterruptedException {
        Flux.range(1, 10)
                .parallel(2)
                .runOn(Schedulers.parallel())
                .log().subscribe();
        TimeUnit.MILLISECONDS.sleep(10);
    }
}
