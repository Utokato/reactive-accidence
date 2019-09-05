package cn.utokato.lambda;

import java.util.function.Consumer;
import java.util.function.IntPredicate;

public class A04_FunctionDemo {

    public static void main(String[] args) {
        // 断言函数
        IntPredicate predicate = i -> i > 0;
        System.out.println(predicate.test(-9));

        // jdk 8 中引入了很多基本数据类型的函数接口，可以减少我们对泛型的添加
        // 如：IntPredicate 和 Predicate<Integer> 是等价的
        // IntConsumer
        // 消费接口
        Consumer<String> consumer = s -> System.out.println(s);
        consumer.accept("hahah");

    }

}
