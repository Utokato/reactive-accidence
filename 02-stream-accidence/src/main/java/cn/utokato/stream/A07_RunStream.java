package cn.utokato.stream;

import java.util.Random;
import java.util.stream.Stream;

/**
 * 验证stream的运行机制
 * <p>
 * 1.所有操作是链式调用，一个元素只迭代一次
 * 2.每一个中间操作返回一个新的流
 * 流里面有一个属性sourceStage 指向同一个位置
 * 就是链表的头，Head
 * 3.Head -> nextStage -> nextStage -> ... -> null
 * 4.有状态操作会把无状态操作阶段，单独处理
 * 5.并行环境下，有状态的中间操作不一定能并行操作
 * 6.parallel | sequential 这两个操作也是中间操作(即：也是返回一个stream)
 * 但是它们跟其他的有区别：它们不创建流，它们只修改Head的并行标志
 */
public class A07_RunStream {

    public static void main(String[] args) {
        Random random = new Random();
        // 随机产生数据
        Stream<Integer> stream = Stream.generate(() -> random.nextInt())
                // 产生500个(无限流需要短路操作)
                .limit(10)
                // 第一个无状态操作
                .peek(s -> System.out.println(Thread.currentThread().getName() + ": peek:" + s))
                // 第二个无状态操作
                .filter(s -> {
                    System.out.println(Thread.currentThread().getName() + ": filter:" + s);
                    return s > 100000;
                })
                // 有状态操作
                .sorted((i1, i2) -> {
                    System.out.println(Thread.currentThread().getName() + "： 排序：" + i1 + "," + i2);
                    return i1.compareTo(i2);
                })
                // 又一个无状态操作
                .peek(s -> {
                    System.out.println(Thread.currentThread().getName() + "： peek2:" + s);
                }).parallel();

        // 终止操作
        stream.count();

    }

}
