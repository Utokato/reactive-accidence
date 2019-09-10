package cn.utokato.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.lang.Runnable;

/**
 * subscribe方法中的lambda表达式作用在了每一个数据元素上
 * 此外，Flux和Mono还提供了多个subscribe方法的变体
 * <p>
 * 订阅前什么都不会发生
 * 在前面已经使用过最简单的订阅方式
 * 只有在subscribe以后，数据流才会被触发
 * <p>
 * 当需要处理 Flux 或 Mono 中的消息时，可以通过 subscribe 方法来添加相应的订阅逻辑。
 * 在调用 subscribe 方法时可以指定需要处理的消息类型。
 * 可以只处理其中包含的正常消息，也可以同时处理错误消息和完成消息。
 *
 * @author lma
 * @date 2019/09/10
 */
public class TestReactorSubscribe {

    /**
     * 订阅并触发数据流
     * {@link Flux#subscribe()}
     * <p>
     * 订阅并指定对正常数据元素如何处理
     * {@link Flux#subscribe(Consumer)}
     */
    @Test
    public void testSubscribe1() {
        Flux.just(1, 2, 3, 4, 5, 6).subscribe(System.out::print);
    }

    /**
     * 订阅并定义对正常数据元素和错误信号的处理
     * {@link Flux#subscribe(Consumer, Consumer)}
     */
    @Test
    public void testSubscribe2() {
        Mono.error(new Exception("Some error!")).subscribe(System.out::println, System.err::println);
    }

    /**
     * 订阅并定义对正常数据元素、错误信号和完成信号的处理
     * {@link Flux#subscribe(Consumer, Consumer, Runnable)}
     */
    @Test
    public void testSubscribe3() {
        Flux.just(1, 2, 3, 4, 5, 6).subscribe(System.out::println, System.err::println,
                () -> System.out.println("All is completed!"));
    }

    /**
     * 订阅并定义对正常数据元素、错误信号和完成信号的处理，以及订阅发生时的处理逻辑
     * {@link Flux#subscribe(Consumer, Consumer, Runnable, Consumer)}
     */
    @Test
    public void testSubscribe4() {
        Flux.just(1, 2, 3, 4, 5, 6).subscribe(System.out::println, System.err::println,
                () -> System.out.println("All is completed!"), subscription -> subscription.request(1));
    }

    @Test
    public void testSubscribe() {
        /**
         * 最简单的错误处理策略，将错误信息进行打印
         */
        Flux.just(1, 2)
                .concatWith(Flux.error(new IllegalStateException()))
                .subscribe(System.out::println, System.err::println);

        /**
         * 正常的消息处理相对简单。当出现错误时，有多种不同的处理策略。
         * 第一种策略是通过 onErrorReturn()方法返回一个默认值
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

        /**
         * 对异常进行分类判断，比如根据不同的错误信息向前端返回不同的 http 状态码
         */
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

        /**
         * 当出现错误时，还可以通过 retry 操作符来进行重试。重试的动作是通过重新订阅序列来实现的。
         * 再使用 retry 操作符时可以指定重试的次数。
         */
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .retry(3)
                .subscribe(System.out::println, System.err::println);
    }
}
