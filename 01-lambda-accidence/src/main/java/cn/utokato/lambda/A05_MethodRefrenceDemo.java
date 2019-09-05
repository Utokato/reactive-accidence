package cn.utokato.lambda;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

class Dog {

    private String name = "zxy";
    private int food = 10;

    public Dog() {
    }

    public Dog(String name) {
        this.name = name;
    }

    /**
     * 狗叫，静态方法
     *
     * @param dog
     */
    public static void bark(Dog dog) {
        System.out.println(dog + " 叫了！");
    }

    // 非静态方法 和 静态方法
    // 非静态方法中可以通过this关键字来调用类中的成员变量
    // 而静态方法不可以，这是因为：非静态方法的参数列表中，默认加入了this对象

    /**
     * 吃狗粮
     * <p>
     * jdk 默认会把当前实例传入非静态方法中，参数名为this，位置是第一个 ；所以，我们不用手动添加这个实例。
     *
     * @param num
     * @return 还剩余多少狗粮
     */
    public int eat(Dog this, int num) {
        System.out.println("吃了" + num + "斤狗粮");
        this.food -= num;
        return this.food;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

/**
 * lambda 中的方法引用
 */
public class A05_MethodRefrenceDemo {

    public static void main(String[] args) {
        Dog dog = new Dog("marlonn");
        dog.eat(3);

        @SuppressWarnings("unused")
        Consumer<String> consumer = s -> System.out.println(s);
        // lambda 实质是是一个匿名函数
        // -> 左边是函数的参数，右边是函数的执行体
        // 当函数的执行体中只有一个函数调用，并且函数的参数与 -> 左边一致的话
        // 这个时候就可以缩写成方法引用的形式
        // 方法引用示例
        Consumer<String> con = System.out::println;
        con.accept("消费的字符串：Hello method reference !");

        // 静态方法的方法引用
        Consumer<Dog> conDog = Dog::bark;
        conDog.accept(dog);

        // 非静态方法，使用对象实例完成方法引用
        // Function<Integer, Integer> function = dog::eat;
        // IntFunction<Integer> function = dog::eat;
        // UnaryOperator<Integer> function = dog::eat;
        IntUnaryOperator function = dog::eat;
        // dog = null; 在这里将dog对象赋值为null，下面的代码依然能够正常运行
        // 这是因为Java中只有值传递，也就是说传递的是值，而不是引用
        // 虽然dog这个引用已经置空了，但是当时创建lambda表达式时，传递的是对象实例的值
        // 由于对象实例的值还在，所以能够正常运行
        System.out.println("还剩下：" + function.applyAsInt(3) + "斤狗粮");

        // 非静态方法，也可以使用类名来完成方法引用
        BiFunction<Dog, Integer, Integer> eatFunc = Dog::eat;
        System.out.println("还剩下：" + eatFunc.apply(dog, 1) + "斤狗粮");

        // 构造函数的方法引用
        // 无参构造函数的方法引用
        Supplier<Dog> supplier = Dog::new;
        System.out.println("创建了新对象：" + supplier.get());
        // 带参构造函数的方法引用
        Function<String, Dog> newDogFunc = Dog::new;
        System.out.println("创建了新对象：" + newDogFunc.apply("张狗"));

        // Java 中 深入理解值传递
        List<String> list = new ArrayList<>();
        test(list);
        System.err.println(list); // 输出的结果是：[] ，一个空的list，而不是null
    }

    private static void test(List<String> list) {
        list = null;
    }

}
