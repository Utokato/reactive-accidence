package cn.utokato.funcWayInJdk8.demo;



import cn.utokato.funcWayInJdk8.pojo.*;

import java.util.*;
import java.util.stream.Stream;

import static java.lang.Character.isDigit;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;

/**
 * @author
 * @date 2018/11/19
 */
public class A003_Stream {
    /**
     * 内部迭代 与 外部迭代
     * <p>
     * 外部迭代：
     * for循环其实是一个封装了迭代的语法糖。
     * 在for循环开始执行时，首先调用了iterator方法，产生了一个新的Iterator对象，
     * 进而控制正在迭代过程，这就是外部迭代。
     * 迭代过程需要通过显式调用Iterator对象的hasNext和next方法完成迭代
     */

    List<Artist> allArtists = new ArrayList<>();

    /**
     * for 循环 ，外部迭代
     */
    public void forLoop() {
        int count = 0;
        for (Artist artist : allArtists) {
            if (artist.isFrom("London")) {
                count++;
            }
        }
    }

    /**
     * for 循环的本质
     */
    public void outerIterator() {
        int count = 0;
        Iterator<Artist> iterator = allArtists.iterator();
        while (iterator.hasNext()) {
            Artist artist = iterator.next();
            if (artist.isFrom("London")) {
                count++;
            }
        }
    }

    /**
     * 内部迭代
     * <p>
     * Stream 是用函数式编程方式在集合类上进行复杂操作的工具
     */
    public void innerIterator() {
        long count = allArtists.stream().filter(artist -> artist.isFrom("London")).count();
    }

    /**
     * 惰性求值
     */
    public void lazyEvaluation() {
        // 惰性求值示例 返回的依然是一个流对象
        Stream<Artist> artistStream = allArtists.stream().filter(artist -> artist.isFrom("London"));

        // 惰性求值示例，中间的打印语句并不会执行(返回的依然是一个流对象)
        Stream<Artist> stream = allArtists.stream().filter(artist -> {
            System.out.println(artist.getName());
            return artist.isFrom("London");
        });
    }

    /**
     * 终止操作
     */
    public void terminalEvaluation() {
        long count = allArtists.stream().filter(artist -> {
            System.out.println(artist.getName());
            return artist.isFrom("London");
        }).count();
    }

    /**
     * 只需看它的返回值。
     * 如果返回值是 Stream，那么是惰性求值；
     * 如果返回值是另一个值或为空， 那么就是及早求值(终止操作)。
     * 使用这些操作的理想方式就是形成一个惰性求值的链， 最后用一个及早求值的操作返回想要的结果
     */

    /**
     * 常见的 流操作
     */
    public static void commonStreamOperators() {
        /* collect(toList()) */
        List<String> collected = Stream.of("a", "b", "c").collect(toList());
        assertEquals(asList("a", "b", "c"), collected);

        /* filter */
        List<String> stringsWithDigitBegin = Stream.of("a", "1abc", "abc1").filter(value -> isDigit(value.charAt(0))).collect(toList());
        assertEquals(asList("1abc"), stringsWithDigitBegin);

        /* map */
        List<String> strings = Stream.of("a", "b", "hello").map(string -> string.toUpperCase()).collect(toList());
        assertEquals(asList("A", "B", "HELLO"), strings);

        /* flatMap */
        List<Integer> together = Stream.of(asList(1, 2), asList(3, 4)).flatMap(nums -> nums.stream()).collect(toList());
        assertEquals(asList(1, 2, 3, 4), together);

        /* max and min */
        Track shortestTrack = asList(new Track("Bakai", 524),
                new Track("Violets for Your Furs", 378),
                new Track("Time Was", 451)).stream().min(Comparator.comparing(track -> track.getLength())).get();
        assertEquals(378, shortestTrack.getLength());

        /* reduce */
        /* max/min/count 等都是reduce操作的一种，只是由于更加常用，所以才被纳入了标准库中 */
        int count = Stream.of(1, 2, 3).reduce(0, (initial, element) -> initial + element);
        assertEquals(6, count);

    }

    public static void combineOperator() {
        Album album = new Album();
        Set<String> origins = album.getMusicians().stream()
                .filter(artist -> artist.getName().startsWith("The"))
                .map(artist -> artist.getNationality())
                .collect(toSet());

    }

    /**
     * 原有代码  需要将其整合为 Java8的风格
     *
     * @param albums
     * @return
     */
    public Set<String> findLongTracks(List<Album> albums) {
        Set<String> trackNames = new HashSet<>();
        for (Album album : albums) {
            for (Track track : album.getTracks()) {
                if (track.getLength() > 60) {
                    String name = track.getName();
                    trackNames.add(name);
                }
            }
        }
        return trackNames;
    }


    public Set<String> findLongTracksWithJava8Style(List<Album> albums) {
        return albums.stream()
                .flatMap(album -> album.getTracks().stream())
                .filter(track -> track.getLength() > 60)
                .map(track -> track.getName())
                .collect(toSet());
    }


    public static void main(String[] args) {
    }
}
