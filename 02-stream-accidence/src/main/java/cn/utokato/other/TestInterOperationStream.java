package cn.utokato.other;

import org.junit.Test;

import java.util.Comparator;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

public class TestInterOperationStream {
    /**
     * 测试Stream的中间操作(惰性求值)
     *
     * 中间操作分为三大类
     * 1. 筛选与切片
     * 2. 映射
     * 3. 排序
     */

    @Test
    public void testOne() {
        /**
         * 以无限流为例
         * 1. 筛选与切片
         * 能看到，中间操作返回的都是一些Stream对象
         */
        Stream<Integer> integerStreamFromGen = Stream.generate(() -> new Random().nextInt());

        // 筛选出大于100的对象
        Stream<Integer> integerStreamGT100 = integerStreamFromGen.filter(i -> i > 100);

        // 根据Stream中元素的hashCode() 和 equals() 来判断元素是否相同，去除相同的元素
        Stream<Integer> integerStreamNoSame = integerStreamGT100.distinct();

        // 限制Stream中元素的个数
        Stream<Integer> integerStreamNumIs10 = integerStreamNoSame.limit(10);

        // 丢弃掉前n个元素
        Stream<Integer> integerStreamSkipTwoEle = integerStreamNumIs10.skip(2);

    }

    @Test
    public void testTwo(){
        /**
         * 2. 映射
         */
        Stream<Integer> integerStreamFromGen = Stream.generate(() -> new Random().nextInt());

        // 将Stream中的元素映射为元素的平方
        Stream<Integer> integerStreamWithQuadratic = integerStreamFromGen.map(i -> i * i);

        // 将int映射为double
        DoubleStream doubleStream = integerStreamWithQuadratic.mapToDouble(i -> i.doubleValue());

        // flatMap 与 map
        doubleStream.flatMap((d)->{
            double temp = d * 2;
            return DoubleStream.of(temp);
        });
    }

    @Test
    public void testThree(){
        /**
         * 3. 排序
         */
        Stream<Integer> integerStreamFromGen = Stream.generate(() -> new Random().nextInt());

        // 按照自然顺序排序
        Stream<Integer> integerStreamByNaturalSorted = integerStreamFromGen.sorted();

        // 按比较器顺序排序
        Stream<Integer> integerStreamByComparator = integerStreamFromGen.sorted(new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                return i1 - i2;
            }
        });

        // 上面是匿名内部类，这个使用lambda替代了匿名类
        Stream<Integer> integerStreamBy = integerStreamFromGen.sorted((i1, i2) -> i1 - i2);
    }
}
