package learn.sharding.jdbc.example.aspect;

import learn.sharding.jdbc.example.annotation.ServiceLock;
import learn.sharding.jdbc.example.distributedlock.redis.RedissLockUtil;
import learn.sharding.jdbc.example.util.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁 aop
 *
 * @transaction 中  order 大小的说明
 * https://docs.spring.io/spring/docs/4.3.14.RELEASE/spring-framework-reference/htmlsingle/#transaction-declarative-annotations
 * https://docs.spring.io/spring/docs/4.3.14.RELEASE/javadoc-api/
 */
@SuppressWarnings("Duplicates")
@Aspect
@Component
@Scope
@Slf4j
@Order(1)
//order 越小越是最先执行, 但更重要的是最先执行的最后结束。order默认值是2147483647
// 而@Transaction order是Integer.MAX_VALUE 后执行，但先结束。所以在有本地事务的情况下会先提交事务，然后读取到提交事务后的数据，不会出现脏读
public class LockAspect {

    /**
     * 切入点
     */
    @Pointcut("@annotation(learn.sharding.jdbc.example.annotation.ServiceLock)")
    public void lockAspect() {

    }

    /**
     * 环绕通知
     *
     * @param joinPoint
     * @return
     */
    @Around("lockAspect()")
    public Object around(ProceedingJoinPoint joinPoint) {
        Method method = getTargetMethod(joinPoint);
        // 获取方法上的@ServiceLock 注解
        ServiceLock serviceLock = method.getAnnotation(ServiceLock.class);
        Object[] args = joinPoint.getArgs();
        String lockKey = UUIDUtil.getUUID();// 获取分布式锁 key
        int waitTime = serviceLock.waitTime(); // 获取分布式锁 最多等待时间
        int leaseTime = serviceLock.leaseTime();// 获取分布式锁 上锁后自动释放锁时间
        TimeUnit timeUnit = serviceLock.timeUnit();// 获取分布式锁 锁时间单位
        RedissLockUtil.tryLock(lockKey, waitTime, leaseTime);
        Object obj = null;
        try {
            obj = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            RedissLockUtil.unlock(lockKey);
        }
        return obj;
    }

    public static Method getTargetMethod(ProceedingJoinPoint joinPoint) {
        Method targetMethod = null;
        try {
            String methodName = joinPoint.getSignature().getName();
            Method[] methodArr = joinPoint.getSignature().getDeclaringType().getMethods();
            for (Method method : methodArr) {
                if (method.getName().equals(methodName)) {
                    targetMethod = method;
                    break;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return targetMethod;
    }
}
