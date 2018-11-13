package learn.sharding.jdbc.example.queue.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by sunyong on 2018-11-13.
 */
@Service("redisSender")
public class RedisSender {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    /**
     * 向通道发送消息的方法
     *
     * @param channel
     * @param message
     */
    public void sendChannelMess(String channel, String message) {

        stringRedisTemplate.convertAndSend(channel, message);
    }
}
