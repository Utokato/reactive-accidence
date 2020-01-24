package cn.utokato.other;


import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class LambdaDemo {

    /**
     * 用lambda表达式实现Runnable
     */
    @Test
    public void testLambda1() {
        System.out.println("=== RunnableTest ===");

        // Anonymous Runnable
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello world one!");
            }
        };

        // Lambda Runnable
        Runnable r2 = () -> System.out.println("Hello world two!");

        // Run em!
        r1.run();
        r2.run();
    }

    /**
     * 用lambda表达式实现Runnable
     */
    @Test
    public void testLambda2() {
        System.out.println("=== NewThreadTest ===");

        // old way
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("A thread running in old way");
            }
        }).start();

        // new way
        new Thread(() -> System.out.println("A thread running in new way")).start();

    }

    /**
     * 使用lambda表达式对列表进行迭代
     */
    @Test
    public void testLambda3() {
        List<String> list = Arrays.asList("hello", "hi", "hiyo", "yo");
        // old way
        System.out.println("Print all elements of list in old way");
        for (String string : list) {
            System.out.println(string);
        }

        // new way
        System.out.println("Print all elements of list in new way");
        list.forEach(s -> System.out.println(s));
        list.forEach(System.out::println);
    }

    public void filter(List<String> names, Predicate<String> condition) {
        for (String name : names) {
            if (condition.test(name)) {
                System.out.println(name + " ");
            }
        }
    }

    public void filter2(List<String> names, Predicate<String> condition) {
        names.stream().filter((name) -> (condition.test(name))).forEach((name) -> {
            System.out.println(name + "");
        });
    }

    /**
     * 使用lambda表达式和函数式接口Predicate
     */
    @Test
    public void testLambda4() {
        List<String> languages = Arrays.asList("Java", "Scala", "C++", "Haskell", "Lisp");
        System.out.println("Languages which starts with J :");
        filter(languages, (str) -> str.startsWith("J"));

        System.out.println("Languages which ends with a ");
        filter(languages, (str) -> str.endsWith("a"));

        System.out.println("Print all languages :");
        filter(languages, (str) -> true);

        System.out.println("Print no language : ");
        filter(languages, (str) -> false);

        System.out.println("Print language whose length greater than 4:");
        filter(languages, (str) -> str.length() > 4);
    }

}
