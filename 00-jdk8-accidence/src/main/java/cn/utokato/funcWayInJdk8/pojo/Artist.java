package cn.utokato.funcWayInJdk8.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 创造音乐的个人或团队
 *
 * @author
 * @date 2018/11/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Artist {

    /* 艺术家的名字 */
    private String name;
    /* 乐队的成员，该字段可为空 */
    private List<String> members;
    /* 乐队来自于哪里 */
    private String nationality;

    /* 判断艺术家是否来自于某个国家 */
    public boolean isFrom(String nationality) {
        if (this.nationality.equals(nationality)) {
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
