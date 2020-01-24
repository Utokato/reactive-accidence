package cn.utokato.funcWayInJdk8.pojo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author
 * @date 2018/11/19
 */
@AllArgsConstructor
@NoArgsConstructor
public class Album {
    /* 专辑名称 */
    private String name;
    /* 专辑中所有曲目的列表 */
    private List<Track> tracks;
    /* 参与创作本专辑的艺术家列表 */
    private List<Artist> musicians;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public List<Artist> getMusicians() {
        return musicians;
    }

    public void setMusicians(List<Artist> musicians) {
        this.musicians = musicians;
    }
}
