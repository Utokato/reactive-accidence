package cn.utokato.lambda;

import java.text.DecimalFormat;
import java.util.function.Function;

// 由于lambda只关心输入类型和输出类型，不关心接口名字和方法名字
// 所以下面的接口可以省略
/*interface IMoneyFormat {
	String format(int i);
}*/

class MyMoney {
    private final int money;

    public MyMoney(int money) {
        this.money = money;
    }

/*	public void printMoney(IMoneyFormat moneyFormat) {
		System.out.println("我的存款：" + moneyFormat.format(this.money));
	}*/

    // 使用jdk8 所带的 Function接口 和 这个接口的apply方法
    // 函数接口的存在，使我们可以少定义一些接口
    public void printMoney(Function<Integer, String> moneyFormat) {
        System.out.println("我的存款：" + moneyFormat.apply(this.money));
    }
}

public class A03_MoneyDemo {

    public static void main(String[] args) {
        MyMoney me = new MyMoney(99999999);

        me.printMoney(i -> new DecimalFormat("#,###").format(i));

        // 函数接口存在的另外一个好处就是，支持链式编程
        Function<Integer, String> moneyFormat = i -> new DecimalFormat("#,###").format(i);
        // 函数接口链式编程操作
        me.printMoney(moneyFormat.andThen(s -> "人民币  " + s));

    }
}
