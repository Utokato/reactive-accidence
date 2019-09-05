package cn.utokato.lambda;

import java.util.function.Function;

/**
 * 级联表达式和柯里化
 *
 * 柯里化：将多个参数的函数转换为只有一个参数的函数
 *
 * 柯里化的目的：函数标准化
 *
 * 高阶函数：就是返回函数的函数
 *
 */
public class A08_CurryDemo {

    // 什么是级联表达式呢? 包含了多个 -> 符号的lambda表达式

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main(String[] args) {

        // 实现了 x+y 的级联表达式
        Function<Integer, Function<Integer, Integer>> fun = x -> y -> x + y;
        // System.out.println(fun.apply(2));
        System.out.println(fun.apply(2).apply(8));

        // 实现 x+y+z 的级联表达式
        Function<Integer, Function<Integer, Function<Integer, Integer>>> func = x -> y -> z -> x + y + z;
        Integer result = func.apply(2).apply(4).apply(7);
        System.out.println(result);

        int[] nums = {7, 8, 9};
        Function f = func;
        // 可以看出，函数柯里化后可以进行统一的处理
        for (int i : nums) {
            if (f instanceof Function) {
                Object obj = f.apply(i);
                if (obj instanceof Function) {
                    f = (Function) obj;
                } else {
                    System.out.println("调用结束：结果为" + obj);
                }
            }
        }
    }

}