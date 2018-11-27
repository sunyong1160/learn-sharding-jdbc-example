package learn.sharding.jdbc.example.service.impl;

import learn.sharding.jdbc.example.annotation.RequestLimit;
import learn.sharding.jdbc.example.annotation.ServiceLock;
import learn.sharding.jdbc.example.service.DemoService;
import learn.sharding.jdbc.example.util.UUIDUtil;
import org.springframework.stereotype.Service;
import reactor.core.support.UUIDUtils;

import java.util.UUID;

/**
 * Created by sunyong on 2018-11-20.
 */
@Service
public class DemoServiceImpl implements DemoService {

    private static String uuid = UUIDUtil.getUUID();

    @Override
    @RequestLimit
    @ServiceLock()
    public String demo() {
        System.out.println("购买成功！");
        return "购买成功";
    }

}
