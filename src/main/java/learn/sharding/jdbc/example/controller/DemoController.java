package learn.sharding.jdbc.example.controller;

import learn.sharding.jdbc.example.annotation.Desc;
import learn.sharding.jdbc.example.service.DemoService;
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

    @Desc("desc213")
    @RequestMapping("/demo")
    public String demo() {

        return demoService.demo();
    }
}
