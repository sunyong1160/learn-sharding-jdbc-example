package learn.sharding.jdbc.example.queue.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * Created by sunyong on 2018-11-13.
 */
@Configuration
public class RedisSubListenerConfig {

    @Value("${redis.channel}")
    private String REDISCHANNEL;

    /**
     * 初始化监听器
     *
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter
            listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(REDISCHANNEL));
        return container;
    }

    /**
     * 利用反射来创建监听到消息之后的执行方法
     *
     * @param redisConsumer
     * @return 宋顺 2018-11-13 17:48:57
     * @EnableApolloConfig可以传入多个namespace
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(RedisConsumer redisConsumer) {
        return new MessageListenerAdapter(redisConsumer, "receiveMessage");
    }

    /**
     * 使用默认的工厂初始化redis操作模板
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public StringRedisTemplate redisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }


}
