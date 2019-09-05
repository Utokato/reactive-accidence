package cn.utokato.lambda;

@FunctionalInterface
interface IMath {
    int add(int x, int y);
}

@FunctionalInterface
interface IMath2 {
    int sub(int x, int y);
}

/**
 *  lambda 类型推断
 *
 */
public class A06_TypeDemo {

    @SuppressWarnings("unused")
    public static void main(String[] args) {

        // 变量类型定义
        IMath lambda = (x, y) -> x + y;

        // 数组中的定义
        IMath[] lambdas = { (x, y) -> x + y };

        // 强制转换
        Object lambda2 = (IMath) (x, y) -> x + y;

        // 通过返回类型
        IMath createLambda = createLambda();

        // 常用方式
        A06_TypeDemo typeDemo = new A06_TypeDemo();
        // 当有二义性时，使用强转对应的接口来解决
        typeDemo.test((IMath) (x, y) -> x + y);

    }

    // 注意方法重载的时候
    public void test(IMath math) {

    }

    public void test(IMath2 math) {

    }

    public static IMath createLambda() {
        return (x, y) -> x + y;
    }
}
