package cn.utokato.webflux01;


import java.util.concurrent.TimeUnit;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import reactor.core.publisher.Flux;

public class ReactorDemo {

	public static void main(String[] args) {
		// reactor = jdk8 stream + jdk9 reactive stream
		// Mono 表示 0-1 个元素
		// Flux 表示 0-N 个元素

		String[] strings = { "1", "2", "3" };
		
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

				// 处理完，调用request再请求一个数据  *** 重点***
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

		// jdk8 stream 操作
		Flux.fromArray(strings).map(s -> Integer.parseInt(s))
		// 最终操作，这里就是jdk9的 reactive stream
		.subscribe(subscriber);
	}
}
