package cn.utokato.stream;

import java.util.Random;
import java.util.stream.Stream;

/**
 * stream 中间操作
 */
public class A03_StreamInterOperation {

    public static void main(String[] args) {

        String str = "my name is 007";

        // 把每个单词的长度打印出来
        Stream.of(str.split(" ")).map(s -> s.length()).forEach(System.out::println);
        // 把每个单词的长度(大于2)的打印出来
        Stream.of(str.split(" ")).filter(s -> s.length() > 2).map(s -> s.length()).forEach(System.out::println);

        System.out.println("/**-------------------------------**/");

        // flatMap A -> B属性(是个集合)，最终得到所有A元素里面的所有B属性集合
        //  ***** intStream/longStream并不是Stream的子类，所以需要装箱，使用boxed  *****
        Stream.of(str.split(" ")).flatMap(s -> s.chars().boxed()).forEach(System.out::println);
        System.out.println("/**-------------------------------**/");
        Stream.of(str.split(" ")).flatMap(s -> s.chars().boxed()).forEach(i -> System.out.println((char) i.intValue()));

        // peek 用于debug，是一个中间操作
        // foreach 是终止操作
        System.out.println("/**------------- peek --------------**/");
        Stream.of(str.split(" ")).peek(System.out::println).forEach(System.out::println);
        ;

        // limit 使用，主要用于无限流
        System.out.println("/**------------- limit --------------**/");
        new Random().ints().filter(i -> i > 100 && i < 1000).limit(10).forEach(System.out::println);


    }

}
