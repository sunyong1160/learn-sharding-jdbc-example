package learn.sharding.jdbc.example.controller;

import com.alibaba.fastjson.JSONObject;
import learn.sharding.jdbc.example.kafka.KafkaSender;
import learn.sharding.jdbc.example.model.dto.UserFacade;
import learn.sharding.jdbc.example.service.UserService;
import learn.sharding.jdbc.example.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by sunyong on 2018-09-05.
 * 分表
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private KafkaSender kafkaSender;

    @RequestMapping("/saveUser")
    public void saveUser() {
        kafkaSender.send("1234");
        //User user = new User();
        //user.setId(1L);
        //user.setName("zhangsan");
        //user.setCity("上海");
        //userService.save(user);
    }

    @GetMapping("/add")
    public Object add() {
        for (long i = 0; i < 100; i++) {
            User user = new User();
            user.setId(i);
            user.setCity("深圳");
            user.setName("李四");
            userService.add(user);
        }
        return "success";
    }

    @PostMapping("/queryUser")
    public String queryUser(@RequestBody UserFacade userFacade) {
        List<User> list = userService.queryUser(userFacade);
        return JSONObject.toJSONString(list);
    }

}
