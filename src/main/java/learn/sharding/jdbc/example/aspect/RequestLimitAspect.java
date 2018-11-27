package learn.sharding.jdbc.example.aspect;

import com.google.common.util.concurrent.RateLimiter;
import learn.sharding.jdbc.example.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by sunyong on 2018-11-20.
 */
@Component
@Aspect
@Slf4j
public class RequestLimitAspect {

    // 每秒只发出2个令牌，此处是单进程服务的限流,内部采用令牌捅算法实现
    private static RateLimiter rateLimiter = RateLimiter.create(2);

    //Service层切点  限流
    @Pointcut("@annotation(learn.sharding.jdbc.example.annotation.RequestLimit)")
    public void requestLimit() {

    }

    @Around("requestLimit()")
    public Object around(ProceedingJoinPoint joinPoint) {
        boolean flag = rateLimiter.tryAcquire(1000, TimeUnit.MILLISECONDS);
        Object obj = null;
        try {
            if (flag) {
                obj = joinPoint.proceed();
            }
            System.out.println("短期无法获取令牌，真不幸，排队也瞎排");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return obj;
    }

}
