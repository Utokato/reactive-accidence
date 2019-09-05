package cn.utokato.jdk9;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class A01_FlowDemo {

    public static void main(String[] args) throws Exception {

        // 1.定义发布者，发布的数据类型是Integer
        // 直接使用jdk自带的SubmissionPublisher，它实现了Publisher接口
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<Integer>();

        // 2.定义订阅者
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {

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
                System.out.println("接收到数据：" + item);

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 处理完，调用request再请求一个数据   *** 重点***
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
                System.out.println("处理完了！");
            }

        };

        // 3.发布者和订阅者 建立订阅关系
        publisher.subscribe(subscriber);

        // 4.生成数据，并发布
        /**
         * submit 是一个block方法；
         * 订阅者的subscription中维护了一个默认32长度的array，最大容量为256
         * 会将发布者submit的数据缓存在这个array，
         * 如果这个array满了，后面submit提交的数据就会阻塞，
         * 这时候，发布者就会停下来，不会再生产数据
         */
        for (int i = 0; i < 500; i++) {
            System.out.println("发布者生产的数据： " + i);
            publisher.submit(i);
        }

        // 5.结束后 关闭发布者
        // 正式环境，应该放finally 或使用 try-resource 去保证关闭
        publisher.close();

        // 主线程延时停止，否则数据没有消费就退出
        Thread.currentThread().join(30000);

    }

}

