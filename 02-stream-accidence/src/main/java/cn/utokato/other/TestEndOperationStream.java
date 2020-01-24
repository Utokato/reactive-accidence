package cn.utokato.other;

import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestEndOperationStream {
    /**
     * Stream 的终止操作
     * 可以看出，终止操作返回的都不是流对象
     * 并且，经过一个终止操作后，Stream对象就会关闭
     * 分为3大类
     * 1. 匹配与查找
     * 2. 归约 - reduce
     * 3. 收集 - collect
     */

    @Test
    public void testOne() {
        /**
         * 1. 匹配与查找
         * 以整数Stream为例
         * 先通过中间操作distinct()去除重复的元素
         * 再通过中间操作limit(10)限制Stream中元素的个数
         */
        Stream<Integer> intStream = Stream.generate(() -> new Random().nextInt()).distinct().limit(10);

        // 是否所有元素都大于100
        // boolean isAllGT100 = intStream.allMatch(i -> i > 100);

        // 是否有一个元素大于100
        // boolean isAnyGT100 = intStream.anyMatch(i -> i > 100);

        // 是否所有的元素都不大于100
        // boolean isNoneGT100 = intStream.noneMatch(i -> i > 100);

        // 返回第一个对象 ，在这里说下 optional 也是Java8 引入的一个对象，用来解决null的异常
        // Optional<Integer> firstElement = intStream.findFirst();

        // 返回任意一个元素
        // Optional<Integer> anyElement = intStream.findAny();

        // 返回Stream中元素的个数
        // long count = intStream.count();

        // 返回Stream中最小值
        // Optional<Integer> min = intStream.min((i1, i2) -> i2 - i1);

        // 返回Stream中最大值
        // Optional<Integer> max = intStream.max((i1, i2) -> i2 - i1);

        intStream.forEach(i -> System.out.println(i));

    }

    @Test
    public void testTwo(){
        /**
         * 归约 - reduce
         */

        Stream<Integer> intStream = Stream.generate(() -> new Random().nextInt()).distinct().limit(10);

        // Optional<Integer> optionalInteger = intStream.reduce((i1, i2) -> i1 + i2);

        Integer reduceInt = intStream.reduce(0, (i1, i2) -> i1 + i2);

        System.out.println(reduceInt);
    }

    @Test
    public void testThree(){
        /**
         * 3. 收集 - collect
         */
        Stream<Integer> intStream = Stream.generate(() -> new Random().nextInt()).distinct().limit(10);

        List<Integer> integerList = intStream.collect(Collectors.toList());

        integerList.forEach(i -> {
            String temp = i + "hahah";
            System.out.println(temp);
        });

    }
}
