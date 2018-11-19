package learn.sharding.jdbc.example.jdk8;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by sunyong on 2018-11-19.
 */
@Data
@AllArgsConstructor
public class Student {

    private String name;
    private int score;

    @Override
    public String toString() {
        return "[姓名=" + name + ", 分数=" + score + "]";
    }
}
