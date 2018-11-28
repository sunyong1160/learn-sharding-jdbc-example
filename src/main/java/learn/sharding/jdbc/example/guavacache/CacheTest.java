package learn.sharding.jdbc.example.guavacache;

import com.google.common.cache.*;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.TimeUnit;

/**
 * 谷歌的缓存机制：本地缓存 可以：Zookeeper + Guava loading cache 实现分布式缓存
 * 如代码所示新建了名为caches的一个缓存对象，maximumSize定义了缓存的容量大小，当缓存数量即将到达容量上线时，则会进行缓存回收，回收最近没有使用或总体上很少使用的缓存项。需要注意的是在接近这个容量上限时就会发生，所以在定义这个值的时候需要视情况适量地增大一点。
 * 另外通过expireAfterWrite这个方法定义了缓存的过期时间，写入十分钟之后过期。
 * 在build方法里，传入了一个CacheLoader对象，重写了其中的load方法。当获取的缓存值不存在或已过期时，则会调用此load方法，进行缓存值的计算。
 * 这就是最简单也是我们平常最常用的一种使用方法。定义了缓存大小、过期时间及缓存值生成方法。
 * <p>
 * 如果用其他的缓存方式，如redis，我们知道上面这种“如果有缓存则返回；否则运算、缓存、然后返回”的缓存模式是有很大弊端的。当高并发条件下同时进行get操作，而此时缓存值已过期时，会导致大量线程都调用生成缓存值的方法，比如从数据库读取。这时候就容易造成数据库雪崩。这也就是我们常说的“缓存穿透”。
 * 而Guava cache则对此种情况有一定控制。当大量线程用相同的key获取缓存值时，只会有一个线程进入load方法，而其他线程则等待，直到缓存值被生成。这样也就避免了缓存穿透的危险。
 * 如上的使用方法，虽然不会有缓存穿透的情况，但是每当某个缓存值过期时，老是会导致大量的请求线程被阻塞。而Guava则提供了另一种缓存策略，缓存值定时刷新：更新线程调用load方法更新该缓存，其他请求线程返回该缓存的旧值。这样对于某个key的缓存来说，只会有一个线程被阻塞，用来生成缓存值，而其他的线程都返回旧的缓存值，不会被阻塞。
 * 这里就需要用到Guava cache的refreshAfterWrite方法。如下所示：
 */
public class CacheTest {

    public static void main(String[] args) {
        LoadingCache<String, Integer> cache = CacheBuilder.newBuilder()
                .maximumSize(10) //最多存放10个数据
                .expireAfterWrite(10, TimeUnit.SECONDS) //缓存10s
                .removalListener(new RemovalListener<String, Integer>() {// 设置缓存的移除通知
                    @Override
                    public void onRemoval(RemovalNotification<String, Integer> removalNotification) {
                        System.out.println(removalNotification.getKey() + " was removed, cause is " + removalNotification.getCause());
                    }
                })
                // build方法中可以指定CacheLoader，在缓存不存在时通过CacheLoader的实现自动加载缓存
                .build(new CacheLoader<String, Integer>() {
                    //数据加载，默认返回-1,也可以是查询操作，如从DB查询
                    @Override
                    public Integer load(String s) throws Exception {
                        return -1;
                    }

//                    @Override
//                    public ListenableFuture<Integer> reload(String key, Integer oldValue) throws Exception {
//
//                        return 1;
//                    }
                });

        //只查询缓存，没有命中，即返回null。 miss++
        System.out.println(cache.getIfPresent("key1")); //null

        //put数据，放在缓存中
        cache.put("key1", 1);

        //再次查询，已存在缓存中, hit++
        System.out.println(cache.getIfPresent("key1")); //1

        //失效缓存
        cache.invalidate("key1");

        //失效之后，查询，已不在缓存中, miss++
        System.out.println(cache.getIfPresent("key1")); //null

        try {
            //查询缓存，未命中，调用load方法，返回-1. miss++
            System.out.println(cache.get("key2"));   //-1
            //put数据，更新缓存
            cache.put("key2", 2);
            //查询得到最新的数据, hit++
            System.out.println(cache.get("key2"));    //2
            System.out.println("size :" + cache.size());  //1

            //插入十个数据
            for (int i = 3; i < 13; i++) {
                cache.put("key" + i, i);
            }
            //超过最大容量的，删除最早插入的数据，size正确
            System.out.println("size :" + cache.size());  //10

            // 休眠10秒
            Thread.sleep(20 * 1000);
            System.out.println("========================");
            System.out.println(cache.getIfPresent("key4"));
            //miss++
            System.out.println(cache.getIfPresent("key2"));  //null

            Thread.sleep(5000); //等待5秒
            cache.put("key1", 1);
            cache.put("key2", 2);
            //key5还没有失效，返回5。缓存中数据为key1，key2，key5-key12. hit++
            System.out.println(cache.getIfPresent("key5")); //5

            Thread.sleep(5000); //等待5秒
            //此时key5-key12已经失效，但是size没有更新
            System.out.println("size :" + cache.size());  //10
            //key1存在, hit++
            System.out.println(cache.getIfPresent("key1")); //1
            System.out.println("size :" + cache.size());  //10
            //获取key5，发现已经失效，然后刷新缓存，遍历数据，去掉失效的所有数据, miss++
            System.out.println(cache.getIfPresent("key5")); //null
            //此时只有key1，key2没有失效
            System.out.println("size :" + cache.size()); //2

            System.out.println("status, hitCount:" + cache.stats().hitCount()
                    + ", missCount:" + cache.stats().missCount()); //4,5
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
