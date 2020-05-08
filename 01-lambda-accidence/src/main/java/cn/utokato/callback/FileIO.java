package cn.utokato.callback;


public class FileIO {

    public void saveStrToFile(String fileName, String str, IFileIOCallBack callback) {
        new Thread(() -> {
            try {
                System.out.println("开始向文件中写数据...");
                System.out.println("正在将" + "'" + str + "'" + "写到" + fileName + "文件中...");
                callback.onResult(true);
            } catch (Exception e) {
                e.printStackTrace();
                callback.onResult(false);
            }
        }).start();
    }

}
