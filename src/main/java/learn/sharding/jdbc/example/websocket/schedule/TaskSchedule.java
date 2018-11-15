package learn.sharding.jdbc.example.websocket.schedule;

import learn.sharding.jdbc.example.queue.redis.RedisConsumer;
import learn.sharding.jdbc.example.queue.redis.RedisSender;
import learn.sharding.jdbc.example.websocket.server.SocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Map;

/**
 * Created by sunyong on 2018-11-14.
 */
@Component
@Slf4j
public class TaskSchedule {

    @Autowired
    private RedisSender redisSender;
    @Value("${redis.channel}")
    private String REDISCHANNEL;

    @Scheduled(cron = "0/5 * * * * *")
    public void task() {
        log.info("定时任务task开始......");
        long begin = System.currentTimeMillis();
        double random = Math.random();
        String msg = "[{\"userId\":\"sunyong\",\"content\":\"hello sunyong + " + random + "\"},{\"userId\":\"123\"," +
                "\"content\":\"hello 123 " + random + "\"}]";
        redisSender.sendChannelMess(REDISCHANNEL, msg);
        long end = System.currentTimeMillis();
        log.info("定时任务task结束，共耗时：[" + (end - begin) + "]毫秒");
    }
}
