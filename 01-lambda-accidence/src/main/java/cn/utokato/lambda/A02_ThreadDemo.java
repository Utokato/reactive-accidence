package cn.utokato.lambda;

public class A02_ThreadDemo {

    public static void main(String[] args) {

        // 匿名内部类
        Object target = new Runnable() {
            @Override
            public void run() {
                System.out.println("ok");
            }
        };
        new Thread((Runnable) target).start();

        // jdk8 lambda
        // lambda 表达式 返回 指定接口的对象实例
        Object target2 = (Runnable) () -> System.out.println("ok");
        Runnable target3 = () -> System.out.println("ok");
        System.out.println(target2 == target3); // false
        new Thread((Runnable) target2).start();

    }

}