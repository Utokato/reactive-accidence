package cn.utokato.other;

import org.junit.Ignore;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 书籍《Java 8 函数式编程》课后题
 * <p>
 * 重点注意 map和flatMap 的区别和使用
 *
 * @author marlonn
 */
public class StreamPractice {

    public class Artist {
        private String name;
        private String nationality;
        private int members;

        public int getMembers() {
            return members;
        }

        public void setMembers(int members) {
            this.members = members;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

        public Artist(String name, String nationality) {
            super();
            this.name = name;
            this.nationality = nationality;
        }

        public Artist(String name, String nationality, int members) {
            super();
            this.name = name;
            this.nationality = nationality;
            this.members = members;
        }
    }

    public class Album {
        private String name;
        private List<String> trackList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getTrackList() {
            return trackList;
        }

        public void setTrackList(List<String> trackList) {
            this.trackList = trackList;
        }

        public Album(String name, List<String> trackList) {
            super();
            this.name = name;
            this.trackList = trackList;
        }

        @Override
        public String toString() {
            return "Album [name=" + name + ", trackList=" + trackList + "]";
        }
    }

    /**
     * 编写一个求和函数，计算流中所有数的和
     */
    public static int addUp(Stream<Integer> nums) {
        // reduce 的使用
        return nums.reduce(0, (x, y) -> x + y);
    }

    /**
     * 编写一个函数，参数为艺术家集合，返回一个字符串集合，其中包含了艺术家的姓名与国籍。
     * (艺术家类名为Artist,里面有获得姓名与国籍的get方法getName()与getNationality())
     */
    public static List<String> getArtistNameAndNationality(List<Artist> artists) {
        return artists.stream().map(a -> a.getName() + " -> " + a.getNationality()).collect(Collectors.toList());
    }

    /**
     * 编写一个函数，参数为专辑集合，返回一个由 最多包含3首歌曲的专辑 组成的集合。
     * (专辑类名为Album，里面包含了一个返回本专辑所有歌曲的集合的方法getTrackList())
     */
    public static List<Album> getAlbumsMostThreeTrack(List<Album> albums) {
        return albums.stream().filter(a -> a.getTrackList().size() <= 3).collect(Collectors.toList());
    }

    /**
     * 统计人数
     *
     * @param artists
     * @return
     */
    public static int countAllArtistMembers(List<Artist> artists) {
        return artists.stream().map(Artist::getMembers).reduce(0, Integer::sum);
    }

    /**
     * 计算一个字符串中小写字母的个数
     */
    public static int countLowCaseCharInString(String s) {
        return (int) s.chars().filter(c -> Character.isLowerCase(c)).count();
    }

    /**
     * 在一个字符串列表中， 找出包含最多小写字母的字符串。 对于空列表， 返回 Optional<String> 对象
     */
    public static Optional<String> findMostLowCaseStringInList(List<String> stringList) {
        return stringList.stream().max(Comparator.comparing(StreamPractice::countLowCaseCharInString));

    }

    public static OptionalInt find(List<String> stringList) {
        return stringList.stream().mapToInt(StreamPractice::countLowCaseCharInString).max();
    }

    /**
     * 使用summaryStatistics
     */
    public static void printSummaryStatistics() {
        IntSummaryStatistics summaryStatistics = Stream.of(1, 2, 3, 4, 5, 6).mapToInt(i -> i.byteValue())
                .summaryStatistics();
        double average = summaryStatistics.getAverage();
        System.out.println(average);
    }

    public <Type> List<Type> filter(Type type) {

        return null;
    }

    @Test
    @Ignore
    public void testQuestionOne() {
        StreamPractice.printSummaryStatistics();
    }

    /**
     * val lines = List("hello tom hello jerry", "hello jerry", "hello kitty")
     * 先按空格切分，在压平
     */
    @Test
    @Ignore
    public void testStreamOperatorOne() {
        Arrays.asList("hello tom hello jerry", "hello jerry", "hello kitty").stream()
                .flatMap(s -> Arrays.asList(s.split(" ")).stream()).forEach(System.out::println);
    }

    @Test
    public void testStreamOperatorTwo() {
        List<Integer> intList = Arrays.asList(1, 7, 9, 8, 0, 3, 5, 4, 6, 2);
        // 将intList中每个元素乘以10后生成一个新的集合
        List<Integer> intListMutilTen = intList.stream().map(i -> i * 10).collect(Collectors.toList());
        intListMutilTen.forEach(System.out::println);
        System.out.println("------------");
        // 将intList中的偶数取出来生成一个新的集合
        List<Integer> intListAllEven = intList.stream().filter(i -> i % 2 == 0 && i != 0).collect(Collectors.toList());
        intListAllEven.forEach(System.out::println);
        System.out.println("------------");
        // 将intList排序后生成一个新的集合
        List<Integer> intListSorted = intList.stream().sorted((i, j) -> i - j).collect(Collectors.toList());
        intListSorted.forEach(System.out::println);
        System.out.println("------------");
        // 反转顺序
        List<Integer> intListSortedInverse = intListSorted.stream().sorted((i, j) -> j - i)
                .collect(Collectors.toList());
        intListSortedInverse.forEach(System.out::println);
        System.out.println("------------");
        // 将intList中的元素4个一组,类型为Iterator[List[Int]]
        Map<Boolean, List<Integer>> map = intList.stream().collect(Collectors.groupingBy(i -> i > 3));
        List<Integer> intGTThree = map.get(true);
        List<Integer> intLTEThree = map.get(false);
        // 将多个list压扁成一个List
        List<Integer> intAfter = Stream.of(intGTThree, intLTEThree).flatMap(Collection::stream).collect(Collectors.toList());
        intAfter.forEach(System.out::println);
        System.out.println("------------");
    }
}
