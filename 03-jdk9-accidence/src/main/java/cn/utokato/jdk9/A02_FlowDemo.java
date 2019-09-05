package cn.utokato.jdk9;

import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;

/**
 * Processor 需要继承SubmissionPublisher并实现Processor接口
 * <p>
 * 输入源数据是 integer ，过滤掉小于0的，然后转成字符串发布出去
 */
class MyProcessor extends SubmissionPublisher<String> implements Processor<Integer, String> {

    /**
     * 可以将subscription理解为一种订阅任务
     */
    private Subscription subscription;

    @Override
    public void onSubscribe(Subscription subscription) {
        // 保存订阅关系，需要它来给发布者相应
        this.subscription = subscription;

        // 请求一个数据
        this.subscription.request(1);

    }

    @Override
    public void onNext(Integer item) {
        // 接收到一个数据，处理
        System.out.println("处理器接收到的数据为：" + item);
        // 过滤掉小于0的数据，然后发布出去
        if (item > 0) {
            this.submit("已将 " + item + " 转为字符串");
        }
        // 处理完后，调用request再次请求一个数据
        this.subscription.request(1);

        // 或者说，已经达到了目标，就调用cancel告诉发布者不再接收数据了
        // this.subscription.cancel();

    }

    @Override
    public void onError(Throwable throwable) {
    }

    @Override
    public void onComplete() {
        System.out.println("处理器这边过滤完了！");
    }

}

public class A02_FlowDemo {

    public static void main(String[] args) throws Exception {

        // 1.定义发布者，发布的数据类型是Integer
        // 直接使用jdk自带的SubmissionPublisher，它实现了Publisher接口
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<Integer>();

        // 2.自定义处理器，对数据进行过滤，并转换为String类型
        MyProcessor processor = new MyProcessor();

        // 3.发布者与 处理器之间，建立订阅关系
        publisher.subscribe(processor);

        // 4.定义最终订阅者，消费String类型数据
        Subscriber<String> subscriber = new Subscriber<String>() {

            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                // 保存订阅关系，需要它来给发布者相应
                this.subscription = subscription;

                // 请求一个数据
                this.subscription.request(1);

            }

            @Override
            public void onNext(String item) {
                // 接收到一个数据，处理
                System.out.println("接收到数据：" + item);

                // 处理完，调用request再请求一个数据 *** 重点***
                // 响应式流的关键于此，处理完毕后可以告诉发布者再次发送数据过来
                // 核心在于subscription 中的 request 和 cancel 方法
                this.subscription.request(1);

                // 或者说，已经达到了目标，就调用cancel告诉发布者不再接收数据了
                // this.subscription.cancel();
            }

            @Override
            public void onError(Throwable throwable) {
                // 出现了异常(例如，处理数据的时候产生了异常)
                throwable.printStackTrace();

                // 可以告诉发布者，不再接收数据了
                this.subscription.cancel();
            }

            @Override
            public void onComplete() {
                // 全部数据处理完了 (发布者关闭时触发)
                System.out.println("订阅者处理完了！");
            }

        };

        // 5.处理器和最终订阅者 建立订阅关系
        processor.subscribe(subscriber);

        // 6.生成数据，并发布
        // 这里忽略数据生成过程
        publisher.submit(-123);
        publisher.submit(111);
        publisher.submit(112);

        // 5.结束后 关闭发布者
        // 正式环境，应该放finally 或使用 try-resource 去保证关闭
        publisher.close();

        // 主线程延时停止，否则数据没有消费就退出
        Thread.currentThread().join(10000);

    }

}
