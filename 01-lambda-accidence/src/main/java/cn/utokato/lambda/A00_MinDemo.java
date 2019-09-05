package cn.utokato.lambda;

import java.util.stream.IntStream;

public class A00_MinDemo {
    public static void main(String[] args) {

        // 命令式示例
        int[] nums = {23, 25, 77, -65, 8};

        int min = Integer.MAX_VALUE;

        for (int i : nums) {
            if (i < min) {
                min = i;
            }
        }

        System.out.println(min);

        // jdk8 函数式编程 示例
        int min2 = IntStream.of(nums).parallel().min().getAsInt();
        System.out.println(min2);

        // 20181107 注
        IntStream.of(nums).parallel().min().ifPresent(System.out::println);
    }
}
