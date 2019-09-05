package cn.utokato.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 变量引用
 * <p>
 * 其实lambda表达式，是实现了某些接口的匿名内部类
 * <p>
 * lambda表达式只关心函数的输入和输出，不关心接口和该函数的名称
 * <p>
 * https://github.com/Utokato/Cargo/blob/master/notes/java/pass-value-or-reference-in-java/PassValueOrReferenceInJava.md
 */
public class A07_VarDemo {

    /**
     * 为什么匿名内部类引用外部类的变量，该变量必须是final?
     * <p>
     * 答案还是：Java中参数传参的形式是--传值，而不是传引用(句柄)。
     */
    // jdk8 以前，匿名类引用外面的变量，该变量必须是final
    // jdk8之后，还必须要这么吗？
    // 答案是肯定的，只是jdk自动帮我们默认的添加了final来修饰
    // 这意味着，初始化后的变量就不能修改了，如果修改就会报错
    public static void main(String[] args) {
        String str = "我们的时间";
        // str = "会报错哟"; // 如果在这里修改str 的值就会报错
        Consumer<String> consumer = s -> System.out.println(s + str);
        consumer.accept("now:");

        // ----- Java中参数传参的形式是--传值，而不是传引用 -----
        List<String> list = new ArrayList<>();
        Consumer<String> con = s -> System.out.println(s + list);
        con.accept("1245");
    }

}
