package cn.utokato.stream;

import java.util.stream.IntStream;

public class A01_StreamDemo {

    public static void main(String[] args) {
        int[] nums = {1, 2, 5};
        // 外部迭代
        int sum = 0;
        for (int i : nums) {
            sum += i;
        }
        System.out.println("外部迭代结果为： " + sum);

        // 使用stream的内部迭代
        // map 就是中间操作 (返回stream的操作)
        // sum 就是终止操作 (返回一个副作用)
        // 如何判断是中间操作还是终止操作 ?
        // 根据操作的返回值来判断，返回的是stream就是中间操作，返回的不是stream就是终止操作
        int sum2 = IntStream.of(nums).map(A01_StreamDemo::doubleNum).sum();
        System.out.println("内部迭代结果为： " + sum2);

        System.out.println("惰性求值，就是终止操作没有调用的情况下，中间操作就不会执行");
        // 下面的代码不会执行，由于惰性求值的特性，因为没有调用终止操作，所以中间操作也不会执行
        IntStream.of(nums).map(A01_StreamDemo::doubleNum);
    }

    public static int doubleNum(int i) {
        System.out.println("执行了乘以2");
        return i * 2;
    }
}

