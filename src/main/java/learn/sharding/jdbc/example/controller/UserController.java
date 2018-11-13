package learn.sharding.jdbc.example.controller;

import com.alibaba.fastjson.JSONObject;
import learn.sharding.jdbc.example.queue.kafka.KafkaSender;
import learn.sharding.jdbc.example.model.dto.UserFacade;
import learn.sharding.jdbc.example.queue.redis.RedisSender;
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
    @Autowired
    private RedisSender redisSender;

    @RequestMapping("/kafkaSender")
    public void kafkaSender() {
        String message = "message_";
        for (int i = 0; i < 10; i++) {
            kafkaSender.send(i + "", message + i);
        }
    }

    @RequestMapping("/redisSender")
    public String redisSender() {
        String message = "message_";
        for (int i = 0; i < 10; i++) {
            redisSender.sendChannelMess("test", message + i);
        }
        return "success";
    }

    @RequestMapping("/saveUser")
    public void saveUser() {
        User user = new User();
        user.setId(1L);
        user.setName("zhangsan");
        user.setCity("上海");
        userService.save(user);
    }

    @GetMapping("/add")
    public Object add() {
        //for (long i = 0; i < 100; i++) {
        //    User user = new User();
        //    user.setId(i);
        //    user.setCity("深圳");
        //    user.setName("李四");
        //    userService.add(user);
        //}
        return "success" + 8888;
    }

    @PostMapping("/queryUser")
    public String queryUser(@RequestBody UserFacade userFacade) {
        List<User> list = userService.queryUser(userFacade);
        return JSONObject.toJSONString(list);
    }

}
