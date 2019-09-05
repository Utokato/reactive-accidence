package cn.utokato.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class A02_StreamCreateDemo {

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();

        // 从集合创建
        list.stream();
        list.parallelStream();

        // 从数组创建
        Arrays.stream(new int[] { 2, 4, 5 });

        // 创建数字流
        IntStream.of(1, 2, 3);
        IntStream.rangeClosed(1, 10);

        // 使用random创建一个无限流
        @SuppressWarnings("unused")
        IntStream limit = new Random().ints().limit(10);

        // 自己创建流
        Random random = new Random();
        Stream.generate(() -> random.nextInt()).limit(20);

    }

}
