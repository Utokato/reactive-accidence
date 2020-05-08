package cn.utokato.callback;

import org.junit.Test;

/*
 * 这是回调的一个例子。
 * 回调是异步保存的方法
 * 首先方法a调用文件保存方法，不需要等待。等该方法保存文件后，在回调a的方法通知文件保存成功
 * 这种"我调用你的方法，你再回调我的方法"机制叫做回调机制
 */

public class MyTestForCallBack implements IFileIOCallBack {

    @Test
    public void saveStr() {
        String fileName = "callback.txt";
        String str = "这是一个回调的例子";
        FileIO fileIO = new FileIO();
        fileIO.saveStrToFile(fileName, str, this);
    }

    @Override
    public void onResult(boolean isSave) {
        System.out.println(isSave ? "保存成功" : "保存失败");
    }

}
