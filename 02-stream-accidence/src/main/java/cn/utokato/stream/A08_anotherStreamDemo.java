package cn.utokato.stream;

import java.time.Month;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Stream;

public class A08_anotherStreamDemo {

    public static long commonSum(long n) {
        long result = 0L;
        for (int i = 1; i <= n; i++) {
            result += i;
        }
        return result;
    }

    public static long parallelSum(long n) {
        return Stream.iterate(1L, i -> i + 1).limit(n).parallel().reduce(0L, Long::sum);
    }

    public static void main(String[] args) {
//		long now = new Date().getTime();
//		System.out.println("开始运行时间：" + now);
//		long result = parallelSum(999999);
//		System.out.println("执行结果：" + result);
//		long end = new Date().getTime();
//		System.out.println("结束执行时间：" + end + " , 消耗时间为： " + (end - now));
//		System.out.println();

//		Stream.of(Month.values()).forEach(m -> {
//			System.out.println(m + "是:" + m.getValue() + "月");
//		});

//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				System.out.println("匿名内部类啊！");
//			}
//		}).start();
//
//		new Thread(() -> System.out.println("lambda表达式啊！")).start();
//
//		Function<Integer, String> function = i -> i.toString();
//		String three = function.apply(3);

//		Function<String, Integer> f = i -> i.length();
//		String str = "marlonn";
//		Integer strLength = f.apply(str);
//		System.out.println(str + "的长度为：" + strLength);

        Function<Integer, Integer[]> function = Integer[]::new;
        Integer[] ints = function.apply(10);

    }

}
