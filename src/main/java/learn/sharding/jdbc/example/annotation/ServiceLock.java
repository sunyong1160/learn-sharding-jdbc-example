package learn.sharding.jdbc.example.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 业务锁
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ServiceLock {

    /**
     * 分布式锁 key 唯一
     *
     * @return
     */
    String lockKey() default "";

    /**
     * 最多等待时间
     *
     * @return
     */
    int waitTime() default 3;

    /**
     * 上锁后自动释放锁时间
     *
     * @return
     */
    int leaseTime() default 10;

    TimeUnit timeUnit() default TimeUnit.SECONDS;

}

