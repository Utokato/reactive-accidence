package cn.utokato.lambda;

@FunctionalInterface
        // jdk8 函数接口 (单一责任值，一个接口只干一件事)
interface Interface1 {
    // 要求：接口只有一个需要实现的方法
    int doubleNum(int i);

    // jdk8 接口中 默认实现的方法
    default int add(int x, int y) {
        return x + y;
    }
}

@FunctionalInterface
interface Interface2 {
    int doubleNum(int i);

    default int add(int x, int y) {
        return x + y;
    }
}

@FunctionalInterface
interface Interface3 extends Interface1, Interface2 {

    // 覆盖默认方法
    // 当继承的多个接口中有相同的默认实现方法时，需要指明覆盖哪一个默认实现的方法
    @Override
    default int add(int x, int y) {
        // TODO Auto-generated method stub
        return Interface1.super.add(x, y);
    }

}

public class A01_LambdaDemo1 {

    @SuppressWarnings("unused")
    public static void main(String[] args) {

        /**
         * 20181107 从下面的代码中可以看出：lambda是函数式接口的一个实例
         */

        Interface1 i1 = (i) -> i * 2;
        // 这是最常见的写法。由于只有一个参数，所以括号可以省略
        Interface1 i2 = i -> i * 2;

        Interface1 i3 = (int i) -> i * 2;

        Interface1 i4 = (int i) -> {
            System.out.println("--->>");
            return i * 2;
        };

        int addResult = i1.add(3, 4);
        System.out.println(addResult);
        System.out.println(i1.doubleNum(20));
    }
}
