package cn.utokato.lambda;

/**
 * Lambda 中的 this 关键字
 *
 * @author lma
 * @date 2019/09/12
 * @modifyDate 2019/09/19
 */
public class ThisDemo {
    private String name = "JustTheName";

    /**
     * 对于匿名内部类而言，最终会生成一个新的类文件
     * 由于this总是指向当前类，所以指向了新类的作用空间
     */
    public void testAnonymousInnerClass() {
        new Thread(new Runnable() {
            String name = "TheInnerName";

            @Override
            public void run() {
                System.out.println("匿名内部类的this指向匿名类:" + this.name);
            }
        }).start();

    }

    /**
     * 对于Lambda而言，最后会在本类中生成一个方法
     * 由于this总是指向当前类，所以指向了外层类的作用空间
     */
    public void testLambda() {
        new Thread(() -> {
            String name = "TheLambdaName"; // 不能通过 this.name 访问到该变量
            System.out.println(name); // 如果想要访问 lambda 表达式中的变量，直接使用变量名，不能使用 this.变量名的形式
            System.out.println("Lambda中的this指向当前的ThisDemo类:" + this.name);
        }).start();
    }

    public static void main(String[] args) {
        ThisDemo demo = new ThisDemo();
        demo.testAnonymousInnerClass(); // TheInnerName
        demo.testLambda(); // JustTheName
    }
}
