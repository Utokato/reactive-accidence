package cn.utokato.funcWayInJdk8.demo;

import java.awt.event.ActionListener;
import java.util.function.BinaryOperator;

/**
 * @author
 * @date 2018/11/19
 */
public class A001_LambdaType {

    /**
     * Lambda表达式 --- 一种紧凑的、传递行为的方式
     */

    /**
     * lambda 表达式的不同形式
     */
    public static void main(String[] args) {

        /* 空参lambda表达式 */
        Runnable noArguments = () -> System.out.println("Hello world!");

        /* 单个lambda表达式 */
        ActionListener oneArguments = event -> System.out.println("button clicked!");

        /* 多返回值lambda表达式 */
        Runnable multiStatement = () -> {
            System.out.print("Hello");
            System.out.println(" World");
        };

        /* 二元lambda表达式 */
        BinaryOperator<Long> add = (x, y) -> x + y;

        /* 手动声明变量类型，二元lambda表达式 */
        BinaryOperator<Long> addExplicit = (Long x, Long y) -> x + y;

    }
}
