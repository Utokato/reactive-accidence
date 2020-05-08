package cn.utokato.stream;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * {@link Stream#forEach(Consumer)}
 * 有两个具体的实现
 * -    1.{@link java.util.stream.ReferencePipeline#forEach(Consumer)}
 * -        当Stream中不仅有流的源头，还有一系列的中间操作时，执行ReferencePipeline中forEach方法
 * -    2.{@link java.util.stream.ReferencePipeline.Head#forEach(Consumer)}
 * -        当Stream中只有流的源头，而没有中间操作时，执行Head中的forEach方法
 */
public class StreamTest3 {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("hello", "world", "hello world", "welcome", "person", "people");

        // list.stream().parallel().forEach(System.out::println);

        // list.stream().forEach(System.out::println);

        list.stream().map(item -> item).filter(item -> true).forEach(System.out::println);


        System.out.println("------");

        list.stream().forEach(System.out::println);

        System.out.println("------");

        list.forEach(System.out::println);


    }
}
