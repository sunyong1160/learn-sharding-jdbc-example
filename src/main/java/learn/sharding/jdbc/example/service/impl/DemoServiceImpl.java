package learn.sharding.jdbc.example.service.impl;

import learn.sharding.jdbc.example.annotation.RequestLimit;
import learn.sharding.jdbc.example.service.DemoService;
import org.springframework.stereotype.Service;

/**
 * Created by sunyong on 2018-11-20.
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Override
    @RequestLimit
    public String demo() {
        System.out.println("购买成功！");
        return "购买成功";
    }

}
