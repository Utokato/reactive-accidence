package cn.utokato.stream;


import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 并行流 Stream
 */
public class A05_ParallelStream {

    public static void main(String[] args) {
        // IntStream.range(1, 100).peek(A05_ParallelStream::debug).count();
        // 调用parallel 产生一个并行流
        // IntStream.range(1, 100).parallel().peek(A05_ParallelStream::debug).count();

        // 现在要实现一个这样的效果：先并行，再串行
        // 多次调用 parallel|sequential，以最后一次为准
        /*	IntStream.range(1, 100)
				// 调用parallel产生并行流
				.parallel().peek(A05_ParallelStream::debug)
				// 调用sequential产生串行流
				.sequential().peek(A05_ParallelStream::debug2)
				.count();*/

        // 并行流使用的线程池：ForkJoinPool.commonPool
        // 默认的线程数：当前机器的CPU个数
        // 使用如下属性可以修改默认的线程数
        // System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
        // IntStream.range(1, 100).parallel().peek(A05_ParallelStream::debug).count();

        // 使用自定义的线程池，不使用默认线程池，放置任务被阻塞
        // 线程池的名字叫：ForkJoinPool-1
        ForkJoinPool pool = new ForkJoinPool(20);
        pool.submit(() -> IntStream.range(1, 100).parallel()
                .peek(A05_ParallelStream::debug).count());
        pool.shutdown();


        // 让主线程等待
        synchronized (pool) {
            try {
                pool.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    public static void debug(int i) {
        System.out.println(Thread.currentThread().getName() + ": debug1: " + i);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void debug2(int i) {
        System.err.println("debug2: " + i);
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
