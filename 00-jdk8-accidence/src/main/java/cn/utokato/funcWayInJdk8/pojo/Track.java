package cn.utokato.funcWayInJdk8.pojo;

/**
 * 专辑中的曲目
 *
 * @author
 * @date 2018/11/19
 */
public class Track {
    /* 曲目名称 */
    private final String name;
    /* 曲目长度 */
    private final int length;

    public Track(String name, int length) {
        this.name = name;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public Track copy() {
        return new Track(name, length);
    }


}
