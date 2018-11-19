package learn.sharding.jdbc.example.jdk8;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunyong on 2018-11-19.
 */
public class InitData {

    public static List<Student> getStudents() {
        List<Student> stuList = new ArrayList<>(10);
        stuList.add(new Student("刘一", 85));
        stuList.add(new Student("陈二", 90));
        stuList.add(new Student("张三", 98));
        stuList.add(new Student("李四", 88));
        stuList.add(new Student("王五", 83));
        stuList.add(new Student("赵六", 95));
        stuList.add(new Student("孙七", 87));
        stuList.add(new Student("周八", 84));
        stuList.add(new Student("吴九", 100));
        stuList.add(new Student("郑十", 95));
        return stuList;
    }
}
