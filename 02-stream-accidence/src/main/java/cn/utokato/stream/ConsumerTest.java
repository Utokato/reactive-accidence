package cn.utokato.stream;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

/**
 * {@link java.util.Spliterator.OfInt#tryAdvance(Consumer)}
 * <p>
 * {@code
 * -    default boolean tryAdvance(Consumer<? super Integer> action) {
 * -        if (action instanceof IntConsumer) {
 * -            return tryAdvance((IntConsumer) action);
 * -        }
 * -    else {
 * -        if (Tripwire.ENABLED)
 * -            Tripwire.trip(getClass(),
 * -                "{0} calling Spliterator.OfInt.tryAdvance((IntConsumer) action::accept)");
 * -            return tryAdvance((IntConsumer) action::accept);
 * -        }
 * -    }
 * }
 * <p>
 * 在理解上述的代码中出现了一些疑问，故作如下解释：
 * -    首先，在将函数作为一等公民的语言中，lambda表达式的类型是函数
 * -    但在Java中，lambda表达式时对象，它们必须依附于一类特别的对象类型 --- 函数式接口(functional interface)
 * -    正是由于这个原因，我们可以将一个lambda表达式赋值给一个对象，如：
 * -    Consumer<Integer> consumer = i->System.out.println(i); 将一个lambda表达式赋值给{@link Consumer}
 * -    {@link Consumer}接口表示接受一个元素，不返回任何结果
 * -    {@link IntConsumer}接口同样表示接受一个元素，不返回任何结果
 * -    只是{@link Consumer}接受一个泛型作为参数，而{@link IntConsumer}接受一个整型作为参数
 * -    这个参数的类型是通过上下文去推断的
 * -    如果上下文推断一个参数是整型，那个这个lambda表达式既可以赋值给{@link Consumer}，也可以赋值给{@link IntConsumer}
 * -    值得注意的是，{@link Consumer}和{@link IntConsumer}之间并没有继承关系
 * -    只是恰好在参数为整型时，lambda表达式同时适合于二者。
 * -    <p>
 * -    {@link this#test(Consumer)}方法接受一个泛型为整型的consumer对象
 * -    定义了两个对象：Consumer<Integer> consumer = i->System.out.println(i);
 * -                  IntConsumer intConsumer = i->System.out.println(i);
 * -    lambda表达式完全一样
 * -    按照面向对象的思想来看，{@link this#test(Consumer)}可以传入一个consumer，但是不可以传入intConsumer
 * -    因为二者之间并不存在继承的关系，所编译通不过
 * -    同样的，当我们在{@link this#testInt(IntConsumer)}中传入一个consumer，也会出现编译出错
 * -    <p>
 * -    但是，当我们以函数式的方式去传递时，
 * -    consumerTest.testInt((IntConsumer) consumer::accept)，传入的是一个方法引用，也就是一个lambda表达式，即一种行为
 * -    只要这种行为满足运行时的上下文定义时，就可以顺利编译通过。
 * -    由于我们的例子中，都是针对于整型进行操作，所以Consumer<Integer> 和 IntConsumer 需求一致，都可以通过传递行为来完成
 * -    这种行为就是传入一个整型，不返回任何结果
 * -    <p>
 * -    再来看如下的代码：
 * -        default boolean tryAdvance(Consumer<? super Integer> action) {
 * -            if (action instanceof IntConsumer) {
 * -                return tryAdvance((IntConsumer) action);
 * -            }
 * -        else {
 * -            if (Tripwire.ENABLED)
 * -                Tripwire.trip(getClass(),
 * -                    "{0} calling Spliterator.OfInt.tryAdvance((IntConsumer) action::accept)");
 * -                return tryAdvance((IntConsumer) action::accept);
 * -            }
 * -        }
 * -    tryAdvance接收一个Consumer对象，如果我们传入了一个lambda表达式
 * -    如 i->System.out.println(i)，也就是action = i->System.out.println(i)
 * -    根据上下文的推断，这个action可以被编译期理解为一个IntConsumer，所以直接进入了if的分支
 * -    即我们在这里传递的是一种行为
 * -    如果我们实现定义了一个对象，如 Consumer<Integer> consumer = i->System.out.println(i);
 * -    再将这个consumer对象传递到tryAdvance方法中，即：action= consumer
 * -    action此时肯定不是一个IntConsumer实例，所以进入了else分支
 * -    在else分支中，执行action::accept，此时action::accept是一个方法引用，这个方法引用本质上就是一个lambda表达式
 * -    可以理解为，从对象中将这个行为抽离出来，既然这个行为满足IntConsumer的要求，所以就可以进行强制类型的转换
 * -    <p>
 * -    可以理解为，上述代码是JDK在面向对象和面向函数两种编程范式之间做出的一种折中决定
 * -    兼容了以前的面向对象编程中传递对象的习惯，同时又支持传递行为的这种函数式编程范式
 * -    同时，我们可以看出，如果我们都使用函数式编程的范式，此处都会直接进入if的分支中，而不会进入else分支由JDK帮我们完成行为的抽取
 * -    所以，在使用JDK8提供的函数式编程中，遇到方法参数为函数式接口的时候，直接传递lambda表达式或方法引用
 * -    不要多此一举的将一个lambda表达式或方法引用指向一个对象，再将对象传递到该方法中
 * -    虽然不会报错，但是伴随着JDK的行为抽取，都会带来性能的损耗
 * -    一句话，函数式编程要纯粹。
 */
public class ConsumerTest {
    public void test(Consumer<Integer> consumer) {
        consumer.accept(100);
    }

    public void testInt(IntConsumer intConsumer) {
        intConsumer.accept(100);
    }

    public static void main(String[] args) {
        ConsumerTest consumerTest = new ConsumerTest();

        Consumer<Integer> consumer = i -> System.out.println(i);
        IntConsumer intConsumer = i -> System.out.println(i);

        System.out.println(consumer instanceof Consumer);
        System.out.println(intConsumer instanceof IntConsumer);
        System.out.println(intConsumer instanceof Consumer);

        consumerTest.test(consumer);            // 面向对象方式
        consumerTest.test(consumer::accept);    // 函数式方式
        consumerTest.test((Consumer<Integer>) intConsumer::accept); // 函数式方式

        System.out.println("----------");

        consumerTest.testInt(intConsumer);
        consumerTest.testInt(intConsumer::accept);
        consumerTest.testInt((IntConsumer) consumer::accept);


    }
}
