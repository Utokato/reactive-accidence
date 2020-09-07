package cn.utokato.rsocket.demo1;

import org.junit.Test;

/**
 * @author lma
 * @date 2020/03/16
 */
public class TestChangeDelivery {

    /**
     * Change Delivery 变化传递
     * 什么时变化传递?
     * -    反应式编程来源于数据流和变化的传播，意味着由底层的执行模型负责通过数据流来自动传播变化。
     * -    比如求值一个简单的表达式 c=a+b，当 a 或者 b 的值发生变化时，传统的编程范式需要对 a+b 进行重新计算来得到 c 的值。
     * -    如果使用反应式编程，当 a 或者 b 的值发生变化时，c 的值会自动更新。
     */
    @Test
    public void testChangeDelivery() {
        int a = 1;
        int b = 2;
        int c = a + b;


        // 3
        System.out.println(c);

        a = 9;

        // 3
        System.out.println(c);
    }

    @Test
    public void testChars() {
        String s = "hello";
        s.chars().forEach(System.out::println);
    }
}
