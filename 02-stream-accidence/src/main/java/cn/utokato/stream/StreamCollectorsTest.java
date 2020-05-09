package cn.utokato.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@link Stream#collect(Collector)}
 * {@link Collectors} 是一个工具类，提供了多变的操作，便于收集操作
 *
 * @author lma
 * @date 2020/05/08
 */
public class StreamCollectorsTest {

    private List<Person> people = Arrays.asList(
            new Person(1, "malong", "malong@163.com", 1, 9000.0),
            new Person(2, "zhangsan", "zhangsan@163.com", 1, 10000.0),
            new Person(3, "malong", "malong@163.com", 1, 11000.0),
            new Person(4, "zhangsan", "zhangsan@163.com", 1, 12000.0),
            new Person(5, "malong", "malong@163.com", 2, 13000.0)
    );

    /**
     * Accumulate names into a List
     */
    @Test
    public void test1() {
        List<String> names = people.stream().map(Person::getName).collect(Collectors.toList());
        System.out.println(names);
    }

    /**
     * Accumulate names into a TreeSet
     */
    @Test
    public void test2() {
        TreeSet<String> names = people.stream().map(Person::getName).collect(Collectors.toCollection(TreeSet::new));
        System.out.println(names);
    }

    /**
     * Convert elements to strings and concatenate them, separated by commas
     */
    @Test
    public void test3() {
        String joined1 = people.stream().map(Person::getName).collect(Collectors.joining());
        String joined2 = people.stream().map(Person::getName).collect(Collectors.joining(","));
        String joined3 = people.stream().map(Person::getName).collect(Collectors.joining(",", "[", "]"));
        System.out.println(joined1);
        System.out.println(joined2);
        System.out.println(joined3);
    }


    /**
     * Compute sum of salaries of employee
     */
    @Test
    public void test4() {
        Double aDouble = people.stream().collect(Collectors.summingDouble(Person::getSalary));
        System.out.println(aDouble);
        Double bDouble = people.stream().mapToDouble(Person::getSalary).sum();
        System.out.println(bDouble);
    }


    /**
     * Group by
     */
    @Test
    public void test5() {
        Map<Integer, List<Person>> map = people.stream().collect(Collectors.groupingBy(Person::getState));
        System.out.println(map);

        Map<Integer, Double> collect = people.stream().collect(Collectors.groupingBy(Person::getState, Collectors.summingDouble(Person::getSalary)));
        System.out.println(collect);
    }

    /**
     * Partition by
     */
    @Test
    public void test6() {
        Map<Boolean, List<Person>> collect = people.stream().collect(Collectors.partitioningBy(p -> p.getSalary() > 10000.0));
        System.out.println(collect);
    }


    @Test
    public void test() {
        /**
         * 根据 Person 的 name 和 state 进行去重
         */
        ArrayList<Person> result = people.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(
                                Comparator.comparing(item -> item.getName() + item.getState())
                        )), ArrayList::new)
        );

        TreeSet<Person> collect = people.stream().collect(Collectors.toCollection(
                () -> new TreeSet<>(
                        Comparator.comparing(item -> item.getName() + item.getState())
                )
        ));

        System.out.println(collect);

        System.out.println(result);
    }

    @Test
    public void testTreeSet(){
        TreeSet<Person> personTreeSet = new TreeSet<>((o1, o2) -> {
            String i = o1.getName() + o1.getState();
            String j = o2.getName() + o2.getState();
            return i.compareTo(j);
        });

        personTreeSet.addAll(people);

        System.out.println(personTreeSet);
    }

}


@Data
@AllArgsConstructor
class Person {
    private int id;
    private String name;
    private String email;
    private int state;
    private double salary;
}
