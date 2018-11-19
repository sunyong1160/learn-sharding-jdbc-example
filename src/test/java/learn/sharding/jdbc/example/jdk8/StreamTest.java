package learn.sharding.jdbc.example.jdk8;

import learn.sharding.jdbc.example.model.TempUser;
import learn.sharding.jdbc.example.model.User;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sunyong on 2018-11-15.
 */
public class StreamTest {

    @Test
    public void test() {

        String str = "1,2,3,4,5,6,1";
        Set<String> hashSet = new HashSet<>(Arrays.asList(str.split(",")));
        // 将String类型转换成Integer类型
        Set<Integer> set = hashSet.stream().map(s -> Integer.valueOf(s)).collect(Collectors.toSet());
        System.out.println(set);
        List<String> lsit = Arrays.asList(str.split(","));
        System.out.println(lsit);
        // 把数组中等于1去掉
        List<String> l1 = lsit.stream().filter(t -> !t.equals("1")).collect(Collectors.toList());
        System.out.println(l1);
        str = "a,v,c,d,f";
        List<String> l2 = Arrays.asList(str.split(","));
        // map 是做某种转换
        List<String> l3 = l2.stream().map(t -> t.toUpperCase()).collect(Collectors.toList());
        System.out.println(l3);
    }

    @Test
    public void test1() {
        List<User> list = Arrays.asList(new User(1, "jack"), new User(2, "rose"), new User(3, "mic"));
        List<TempUser> tempUserList = list.stream().map(temp -> {
            TempUser tempUser = new TempUser();
            tempUser.setId(temp.getId());
            tempUser.setName(temp.getUsername());
            return tempUser;
        }).collect(Collectors.toList());

    }
}
