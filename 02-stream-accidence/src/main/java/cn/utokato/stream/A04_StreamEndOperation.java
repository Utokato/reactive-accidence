package cn.utokato.stream;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * stream 终止操作
 */
public class A04_StreamEndOperation {

    public static void main(String[] args) {

        String str = "my name is 007";

        // 并行流 forEach 在并行流中会出现乱序
        str.chars().parallel().forEach(i -> System.out.print((char) i));
        System.out.println();
        // forEachOrdered 保证顺序
        str.chars().parallel().forEachOrdered(i -> System.out.print((char) i));
        System.out.println();

        // 收集到list中
        List<String> list = Stream.of(str.split(" ")).collect(Collectors.toList());
        System.out.println(list);
        // 收集到set中
        Set<String> set = Stream.of(str.split(" ")).collect(Collectors.toSet());
        System.out.println(set);

        // reduce 终止操作 || 使用reduce拼接字符串
        Optional<String> letters = Stream.of(str.split(" ")).reduce((s1, s2) -> s1 + "|" + s2);
        System.out.println(letters.orElse(""));

        // 带初始化值的reduce
        String reduce = Stream.of(str.split(" ")).reduce("", (s1, s2) -> s1 + "|" + s2);
        System.out.println(reduce);

        // 计算所有单词的总长度
        Integer len = Stream.of(str.split(" ")).map(s -> s.length()).reduce(0, (s1, s2) -> s1 + s2);
        System.out.println(len);

        // max 的使用
        Optional<String> re = Stream.of(str.split(" ")).max((s1, s2) -> s1.length() - s2.length());
        System.out.println(re.orElse(""));

        // 使用 findFirst 短路操作
        OptionalInt first = new Random().ints().findFirst();
        System.out.println(first.orElse(0));
    }

}
