package cn.utokato.stream;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.collections.MapUtils;

enum Gender {
    MALE, FEMALE;
}

enum Grade {
    ONE, TWO, THREE, FOUR;
}

class Student {
    private String name;
    private int age;
    private Gender gender;
    private Grade grade;

    public Student(String name, int age, Gender gender, Grade grade) {
        super();
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Student [name=" + name + ", age=" + age + ", gender=" + gender + ", grade=" + grade + "]";
    }

}

/**
 * 收集器
 *
 */
public class A06_CollectOfStreamDemo {

    public static void main(String[] args) {

        // 测试数据
        List<Student> students = Arrays.asList(
                new Student("小明", 10, Gender.MALE, Grade.ONE),
                new Student("小明", 11, Gender.MALE, Grade.ONE),
                new Student("大明", 12, Gender.MALE, Grade.TWO),
                new Student("小白", 13, Gender.FEMALE, Grade.THREE),
                new Student("小黑", 14, Gender.MALE, Grade.FOUR),
                new Student("小红", 12, Gender.FEMALE, Grade.THREE),
                new Student("小黄", 16, Gender.MALE, Grade.ONE),
                new Student("小青", 7, Gender.FEMALE, Grade.TWO),
                new Student("小紫", 18, Gender.MALE, Grade.THREE),
                new Student("小李", 12, Gender.FEMALE, Grade.FOUR),
                new Student("小马", 20, Gender.MALE, Grade.ONE),
                new Student("小刘", 17, Gender.FEMALE, Grade.FOUR)
        );

        // 得到所有学生的年龄列表
        // s -> s.getAge() 最好使用方法引用的方式 Student::getAge
        // 因为方法引用，不会多生产一个类似于 lambda$0 这样的函数
        List<Integer> ages = students.stream().map(Student::getAge).collect(Collectors.toList());
        System.out.println("所有学生的年龄： "+ ages);

        TreeSet<Integer> ageSet = students.stream().map(Student::getAge).collect(Collectors.toCollection(TreeSet::new));
        System.out.println("所有学生的年龄： "+ ageSet);

        // *** 统计汇总信息
        IntSummaryStatistics ageSummaryStatistics = students.stream().collect(Collectors.summarizingInt(Student::getAge));
        System.out.println("年龄汇总信息："+ageSummaryStatistics);

        // 分块
        Map<Boolean, List<Student>> genders = students.stream().collect(Collectors.partitioningBy(s -> s.getGender() == Gender.MALE));
        // System.out.println("男女生列表为："+genders);
        MapUtils.verbosePrint(System.out, "男女生列表为：", genders);

        // *** 分组
        Map<Grade, List<Student>> grades = students.stream().collect(Collectors.groupingBy(Student::getGrade));
        MapUtils.verbosePrint(System.out, "学生班级列表为：", grades);

        // 得到所有班级学生的个数
        Map<Grade, Long> gradesCount = students.stream().collect(Collectors.groupingBy(Student::getGrade,Collectors.counting()));
        MapUtils.verbosePrint(System.out, "班级学生个数列表为：", gradesCount);

    }

}

