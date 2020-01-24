package cn.utokato.other;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class TestCreateStream {

    /**
     * 创建Stream有四种方式
     * 1. 通过集合
     * 2. 通过数组
     * 3. 通过Stream类的静态方法of()
     * 4. 通过Stream类的静态方法iterate()和generate()创建无限流
     */

    @Test
    public void testOne(){
        /**
         * Collection 接口被扩展，提供了两个获取流的方法
         * 以ArrayList为例演示
         */
        List<Integer> listForStream = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            listForStream.add(i);
        }
        // 创建一个Stream对象
        Stream<Integer> stream = listForStream.stream();
        // 创建一个parallelStream对象
        Stream<Integer> parallelStream = listForStream.parallelStream();

        System.out.println(stream);
        System.out.println(parallelStream);
    }

    @Test
    public void testTwo(){
        /**
         * Arrays 的静态方法 stream() 可以获取数组流
         */
        Integer[] integers = new Integer[]{1,2,3,4,5,6,7,8,9};
        Stream<Integer> integerStream = Arrays.stream(integers);

        System.out.println(integerStream);
    }

    @Test
    public void testThree(){
        /**
         * 通过Stream类的静态方法of()
         */
        Stream<String> stringStream = Stream.of("hello stream my name is marlonn".split(" "));

        System.out.println(stringStream);
    }

    @Test
    public void testFour(){
        /**
         * 通过Stream类的静态方法iterate()和generate()创建无限流
         */
        Stream<Integer> integerStreamFromIter = Stream.iterate(0, i -> i + i);

        Stream<Integer> integerStreamFromGen = Stream.generate(() -> new Random().nextInt());


        System.out.println(integerStreamFromIter);
        System.out.println(integerStreamFromGen);
    }
}
