package learn.sharding.jdbc.example.queue.redis;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by sunyong on 2018-11-13.
 */
@Service
public class RedisConsumer {

    public void receiveMessage(String message) {

        System.out.println(message+"============");
    }

}
