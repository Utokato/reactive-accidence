package cn.utokato.funcWayInJdk8.demo;

import java.util.function.BinaryOperator;
import java.util.function.Predicate;

/**
 * @date 2018/11/19
 */
public class A002_FuncInterface {

    /**
     * 函数式接口：
     *      函数接口是只有一个抽象方法的接口， 用作 Lambda 表达式的类型
     * 反过来说：
     *      一个lambda表达式，是一个函数式接口的实例
     *
     * 函数接口， 接口中单一方法的命名并不重要， 只要方法签名和 Lambda 表达式的类型匹配即可
     */

    /**
     * Java 中重要的函数式接口
     *
     * 接口                       参数          返回类型
     * Predicate<T>               T            boolean
     * Consumer<T>                T             void
     * Function<T,R>              T              R
     * Supplier<T>               None            T
     * UnaryOperator<T>           T              T
     * BinaryOperator<T>         (T,T)           T
     */

    public static void main(String[] args) {
        Predicate<Integer> atLeast5 = x -> x > 5;

        BinaryOperator<Long> addLongs = (x, y) -> x + y;
        // 没有泛型，代码则通不过编译
        // BinaryOperator add = (x, y) -> x + y;

    }


}
