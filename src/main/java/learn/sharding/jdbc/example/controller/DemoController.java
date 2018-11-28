package learn.sharding.jdbc.example.controller;

import com.alibaba.fastjson.JSON;
import learn.sharding.jdbc.example.annotation.Desc;
import learn.sharding.jdbc.example.guavacache.Student;
import learn.sharding.jdbc.example.guavacache.StudentCache;
import learn.sharding.jdbc.example.service.DemoService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sunyong on 2018-11-20.
 */
@RestController
public class DemoController {

    @Autowired
    private DemoService demoService;

    @Autowired
    private StudentCache studentCache;

    @Desc("desc213")
    @RequestMapping("/demo")
    public String demo(@RequestParam("key") String key) {
        Student student = studentCache.getConfig(key);
        return JSON.toJSONString(student);
    }
}
