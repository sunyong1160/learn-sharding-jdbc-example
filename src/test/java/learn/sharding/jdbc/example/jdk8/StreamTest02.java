package learn.sharding.jdbc.example.jdk8;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by sunyong on 2018-11-19.
 */
public class StreamTest02 {

    //map用来归类，结果一般是一组数据，比如可以将list中的学生分数映射到一个新的stream中
    //reduce用来计算值，结果是一个值，比如计算最高分
    public static void main(String[] args) {
        // 初始化list
        List<Student> list = InitData.getStudents();
        // 使用map方法获取list中的name
        List<String> names = list.stream().map(Student::getName).collect(Collectors.toList());
        System.out.println("names:" + names);

        // 使用map方法获取list数据中name的长度
        List<Integer> length = list.stream().map(Student::getName).map(String::length).collect(Collectors.toList());
        System.out.println("length:" + length);

        // 将每人的分数减10
        List<Integer> lists = list.stream().map(Student::getScore).map(i -> i - 10).collect(Collectors.toList());
        System.out.println("lists:" + lists);

        // 计算学生总分
        Integer countScore1 = list.stream().map(Student::getScore).reduce(0, (a, b) -> a + b);
        System.out.println("countScore1:" + countScore1);

        // 计算学生总分，返回Optional类型的数据，改类型是java8中新增的，主要用来避免空指针异常
        Optional<Integer> totalScore2 = list.stream().map(Student::getScore).reduce((a, b) -> a + b);
        System.out.println("totalScore2:" + totalScore2);

        // 计算最高分和最低分
        Optional<Integer> max = list.stream().map(Student::getScore).reduce(Integer::max);
        Optional<Integer> min = list.stream().map(Student::getScore).reduce(Integer::min);
        System.out.println("max:" + max);
        System.out.println("min:" + min);

        // java8中新增了三个原始类型流（IntStream、DoubleStream、LongStream）来解决这个问题
        int sum = list.stream().mapToInt(Student::getScore).sum();
        System.out.println("sum:" + sum);

        // 计算平均数
        OptionalDouble optionalDouble = list.stream().mapToInt(Student::getScore).average();
        double asDouble = optionalDouble.getAsDouble();
        System.out.println("asDouble:" + asDouble);

        //生成1~100之间的数字
        IntStream intStream = IntStream.rangeClosed(1, 100);

        //计算1~100之间数字中偶数的个数
        long count = intStream.filter(i -> i % 2 == 0).count();
        System.out.println("count:" + count);
    }

}
