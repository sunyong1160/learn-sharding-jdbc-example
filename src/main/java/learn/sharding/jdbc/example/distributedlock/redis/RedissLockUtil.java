package learn.sharding.jdbc.example.distributedlock.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * Redission lock
 * 分布式锁
 */
public class RedissLockUtil {

    private static RedissonClient redissonClient;

    public void setRedissonClient(RedissonClient locker) {
        redissonClient = locker;
    }


    /**
     * 加锁
     *
     * @param lockKey
     * @return
     */
    public static RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * 释放锁
     *
     * @param lockKey
     */
    public static void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    /**
     * 释放锁
     *
     * @param lock
     */
    public static void unlock(RLock lock) {
        lock.unlock();
    }

    /**
     * 带超时的锁
     *
     * @param lockKey
     * @param timeout 超时时间，单位：秒
     */
    public static RLock lock(String lockKey, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, TimeUnit.SECONDS);
        return lock;
    }

    /**
     * 带超时的锁
     *
     * @param lockKey
     * @param timeUnit 时间单位
     * @param timeout  超时时间
     * @return
     */
    public static RLock lock(String lockKey, TimeUnit timeUnit, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, timeUnit);
        return lock;
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryLock(String lockKey, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
        }
        return false;
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @param timeUnit  时间单位
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryLock(String lockKey, TimeUnit timeUnit, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, timeUnit);
        } catch (InterruptedException e) {
        }
        return false;
    }

//    @Transactional
//    public Result startSeckilRedisLock(long seckillId,long userId) {
//        boolean res=false;
//        try {
//            /**
//             * 尝试获取锁，最多等待3秒，上锁以后20秒自动解锁（实际项目中推荐这种，以防出现死锁）、这里根据预估秒杀人数，设定自动释放锁时间.
//             * 看过博客的朋友可能会知道(Lcok锁与事物冲突的问题)：https://blog.52itstyle.com/archives/2952/ 这里做了解释
//             * 分布式锁的使用和Lock锁的实现方式是一样的，但是测试了多次分布式锁就是没有问题，当时就留了个坑
//             * 闲来咨询了《静儿1986》，推荐下博客：https://www.cnblogs.com/xiexj/p/9119017.html
//             * 先说明下之前的配置情况：Mysql在本地，而Redis是在外网。
//             * 回复是这样的：
//             * 这是因为分布式锁的开销是很大的。要和锁的服务器进行通信，它虽然是先发起了锁释放命令，涉及网络IO，延时肯定会远远大于方法结束后的事务提交。
//             * ==========================================================================================
//             * 分布式锁内部都是Runtime.exe命令调用外部，肯定是异步的。分布式锁的释放只是发了一个锁释放命令就算完活了。真正其作用的是下次获取锁的时候，要确保上次是释放了的。
//             * 就是说获取锁的时候耗时比较长，那时候事务肯定提交了就是说获取锁的时候耗时比较长，那时候事务肯定提交了。
//             * ==========================================================================================
//             * 周末测试了一下，把redis配置在了本地，果然出现了超卖的情况；或者还是使用外网并发数增加在10000+也是会有问题的，之前自己没有细测，我的锅。
//             * 所以这钟实现也是错误的，事物和锁会有冲突，建议AOP实现。
//             */
//            res = RedissLockUtil.tryLock(seckillId+"", TimeUnit.SECONDS, 3, 20);
//            if(res){
//                String nativeSql = "SELECT number FROM seckill WHERE seckill_id=?";
//                Object object =  dynamicQuery.nativeQueryObject(nativeSql, new Object[]{seckillId});
//                Long number =  ((Number) object).longValue();
//                if(number>0){
//                    SuccessKilled killed = new SuccessKilled();
//                    killed.setSeckillId(seckillId);
//                    killed.setUserId(userId);
//                    killed.setState((short)0);
//                    killed.setCreateTime(new Timestamp(new Date().getTime()));
//                    dynamicQuery.save(killed);
//                    nativeSql = "UPDATE seckill  SET number=number-1 WHERE seckill_id=? AND number>0";
//                    dynamicQuery.nativeExecuteUpdate(nativeSql, new Object[]{seckillId});
//                }else{
//                    return Result.error(SeckillStatEnum.END);
//                }
//            }else{
//                return Result.error(SeckillStatEnum.MUCH);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally{
//            if(res){//释放锁
//                RedissLockUtil.unlock(seckillId+"");
//            }
//        }
//        return Result.ok(SeckillStatEnum.SUCCESS);
//    }
}
