package cn.utokato.other;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author
 * @date 2019/2/13
 */
public class StreamTest2 {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("hello", "world", "hello world");

        // 由于该stream没有调用close方法，所以在onClose中设置的 close handler 方法都没有得到执行
        list.stream()
                .onClose(() -> System.out.println("aaa"))
                .onClose(() -> System.out.println("bbb"))
                .forEach(System.out::println);

        System.out.println("------");

        NullPointerException nullPointerException = new NullPointerException("My exception");


        // 使用try-source的方式，会自动调用stream的close方法，所以onClose中设置的 close handler方法都得到了执行
        // 同时在这两个onClose方法中分别抛出了两个异常，第一个异常会正常打印，第二个及以后的异常会通过Suppressed的方式打印
        // 如果多个onClose方法中抛出的是同一个异常对象，则这个对象是不会被Suppressed，因为一个对象本身是不会Suppressed它自己
        // 即使是相同类型的异常，只要这些异常不是同一个对象，后面的异常都会被第一个异常对象Suppressed
        try (Stream<String> stream = list.stream()) {
            stream.onClose(() -> {
                System.out.println("aaa");
                // throw new NullPointerException("First Exception");
                // throw nullPointerException;
                throw new NullPointerException("First Exception");
            }).onClose(() -> {
                System.out.println("bbb");
                // throw new ArithmeticException("Second Exception");
                // throw nullPointerException;
                throw new NullPointerException("Second Exception");
            }).forEach(System.out::println);

        }
    }
}
