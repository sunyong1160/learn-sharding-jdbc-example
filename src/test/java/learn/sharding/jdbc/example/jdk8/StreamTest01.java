package learn.sharding.jdbc.example.jdk8;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sunyong on 2018-11-19.
 */
public class StreamTest01 {

    public static void main(String[] args) {
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

        //需求：列出90分以上的学生姓名，并按照分数降序排序

        //以前的写法，代码较多，每个操作都需要遍历集合
        List<Student> result1 = new ArrayList<>(10);
        //遍历集合获取分数大于90以上的学生并存放到新的List中
        for (Student s : stuList) {
            if (s.getScore() >= 90) {
                result1.add(s);
            }
        }
        //对List进行降序排序
        result1.sort(new Comparator<Student>() {
            @Override
            public int compare(Student s1, Student s2) {
                //降序排序
                return Integer.compare(s2.getScore(), s1.getScore());
            }
        });
        System.out.println(result1);

        //使用stream的写法
        /*
         * 1.获取集合的stream对象
         * 2.使用filter方法完成过滤
         * 3.使用sort方法完成排序
         * 4.使用collect方法将处理好的stream对象转换为集合对象
         */
        result1 = stuList.stream()
                .filter(s -> s.getScore() >= 90)
                //.sorted((s1,s2) -> Integer.compare(s2.getScore(), s1.getScore()))
                //使用Comparator中的comparing方法
                .sorted(Comparator.comparing(Student::getScore).reversed())
                .collect(Collectors.toList());
        System.out.println(result1);
    }
}
