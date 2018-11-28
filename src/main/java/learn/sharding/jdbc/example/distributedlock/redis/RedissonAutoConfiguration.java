package learn.sharding.jdbc.example.distributedlock.redis;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@ConditionalOnClass(Config.class)
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonAutoConfiguration {

    @Autowired
    private RedissonProperties redssionProperties;

    /**
     * 哨兵模式自动装配
     *
     * @return
     */
//    @Bean
//    @ConditionalOnProperty(name = "redisson.master-name") // 需要配置在application.properties中配置redisson.master-name
//    public RedissonClient redissonSentinel() {
//        Config config = new Config();
//        SentinelServersConfig sentinelServersConfig = config.useSentinelServers().addSentinelAddress(redssionProperties.getSentinelAddresses())
//                .setMasterName(redssionProperties.getMasterName())
//                .setTimeout(redssionProperties.getTimeout())
//                .setMasterConnectionPoolSize(redssionProperties.getMasterConnectionPoolSize())
//                .setSlaveConnectionPoolSize(redssionProperties.getSlaveConnectionPoolSize());
//        if (StringUtils.isNotBlank(redssionProperties.getPassword())) {
//            sentinelServersConfig.setPassword(redssionProperties.getPassword());
//        }
//        return Redisson.create(config);
//    }

    /**
     * 集群模式自动装配
     *
     * @return
     */
//    @Bean
//    @ConditionalOnProperty(name = "redisson.address")
//    public RedissonClient redissonCluster() {
//        String[] nodes = redssionProperties.getAddress().split(",");
//        //redisson版本是3.5，集群的ip前面要加上“redis://”，不然会报错，3.2版本可不加
//        for (int i = 0; i < nodes.length; i++) {
//            nodes[i] = "redis://" + nodes[i];
//        }
//        Config config = new Config();
//        ClusterServersConfig clusterServersConfig = config.useClusterServers().addNodeAddress(nodes)
//                .setScanInterval(2000);//设置集群状态扫描时间
//        if (StringUtils.isNotBlank(redssionProperties.getPassword())) {
//            clusterServersConfig.setPassword(redssionProperties.getPassword());
//        }
//        return Redisson.create(config);
//    }


    /**
     * 单机模式自动装配
     *
     * @return
     * @ConditionalOnProperty 注解的作用是：这个注解能够控制某个configuration是否生效。
     * 具体操作是通过两个属性name和havingValue来实现的，其中name用来从application.properties中读取的某个属性值，如果该值为空，则返回false，如果不为空。则将
     * 该值与havingValue指定的值进行比较，如果一样则返回true，否则返回false，则该configuration不生效，为ture则生效
     */
    @Bean
    @ConditionalOnProperty(name = "redisson.address")
    RedissonClient redissonSingle() {
        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(redssionProperties.getAddress()) // redisson.address 前面不需要加redis://
                .setTimeout(redssionProperties.getTimeout())
                .setConnectionPoolSize(redssionProperties.getConnectionPoolSize())
                .setConnectionMinimumIdleSize(redssionProperties.getConnectionMinimumIdleSize());
        if (StringUtils.isNotBlank(redssionProperties.getPassword())) {
            serverConfig.setPassword(redssionProperties.getPassword());
        }

        return Redisson.create(config);
    }

    /**
     * 装配locker类，并将实例注入到RedissLockUtil中
     *
     * @return
     */
    @Bean
    RedissLockUtil redissLockUtil(RedissonClient redissonClient) {
        RedissLockUtil redissLockUtil = new RedissLockUtil();
        redissLockUtil.setRedissonClient(redissonClient);
        return redissLockUtil;
    }
}
