package learn.sharding.jdbc.example.guavacache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @description: 利用guava实现的内存缓存。缓存加载之后永不过期，后台线程定时刷新缓存值。刷新失败时将继续返回旧缓存。
 * 在调用getValue之前，需要设置 refreshDuration， refreshTimeunit， maxSize 三个参数
 * 后台刷新线程池为该系统中所有子类共享，大小为20.
 */
public abstract class BaseGuavaCache<K, V> {

    private Logger logger = LoggerFactory.getLogger(getClass());
    // 缓存自动刷新周期
    protected int refreshDuration = 10;
    // 缓存刷新周期时间格式
    protected TimeUnit refreshTimeunit = TimeUnit.MINUTES;
    // 缓存过期时间（可选择）
    protected int expireDuration = -1;
    // 缓存刷新周期时间格式
    protected TimeUnit expireTimeunit = TimeUnit.HOURS;
    // 缓存最大容量
    protected int maxSize = 4;
    // 数据刷新线程池
    protected static ListeningExecutorService refreshPool = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(20));

    private LoadingCache<K, V> cache = null;

    /**
     * 用于初始化缓存值（某些场景下使用，例如系统启动检测缓存加载是否征程）
     */
    public abstract void loadValueWhenStarted();

    /**
     * @param key
     * @throws Exception
     * @description: 定义缓存值的计算方法
     * @description: 新值计算失败时抛出异常，get操作时将继续返回旧的缓存
     */
    protected abstract V getValueWhenExpired(K key) throws Exception;

    /**
     * @param key
     * @throws Exception
     * @description: 从cache中拿出数据操作
     */
    public V getValue(K key) throws Exception {
        try {
            return getCache().get(key);
        } catch (Exception e) {
            logger.error("从内存缓存中获取内容时发生异常，key: " + key, e);
            throw e;
        }
    }

    public V getValueOrDefault(K key, V defaultValue) {
        try {
            return getCache().get(key);
        } catch (Exception e) {
            logger.error("从内存缓存中获取内容时发生异常，key: " + key, e);
            return defaultValue;
        }
    }

    /**
     * 设置基本属性
     */
    public BaseGuavaCache<K, V> setRefreshDuration(int refreshDuration) {
        this.refreshDuration = refreshDuration;
        return this;
    }

    public BaseGuavaCache<K, V> setRefreshTimeUnit(TimeUnit refreshTimeunit) {
        this.refreshTimeunit = refreshTimeunit;
        return this;
    }

    public BaseGuavaCache<K, V> setExpireDuration(int expireDuration) {
        this.expireDuration = expireDuration;
        return this;
    }

    public BaseGuavaCache<K, V> setExpireTimeUnit(TimeUnit expireTimeunit) {
        this.expireTimeunit = expireTimeunit;
        return this;
    }

    public BaseGuavaCache<K, V> setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        return this;
    }

    public void clearAll() {
        this.getCache().invalidateAll();
    }

    /**
     * @description: 获取cache实例
     */
    private LoadingCache<K, V> getCache() {
        if (cache == null) {
            synchronized (this) {
                if (cache == null) {
                    CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder().maximumSize(maxSize);
                    if (refreshDuration > 0) {
                        cacheBuilder = cacheBuilder.refreshAfterWrite(refreshDuration, refreshTimeunit);
                    }
                    if (expireDuration > 0) {
                        cacheBuilder = cacheBuilder.expireAfterWrite(expireDuration, expireTimeunit);
                    }
                    cache = cacheBuilder.build(new CacheLoader<K, V>() {
                        @Override
                        public V load(K key) throws Exception {
                            return getValueWhenExpired(key);
                        }

                        @Override
                        public ListenableFuture<V> reload(final K key, V oldValue) throws Exception {
                            return refreshPool.submit(new Callable<V>() {
                                public V call() throws Exception {
                                    return getValueWhenExpired(key);
                                }
                            });
                        }
                    });
                }
            }
        }
        return cache;
    }

    @Override
    public String toString() {
        return "GuavaCache";
    }


    /**
     * 定时过期
     * maximumSize定义了缓存的容量大小，当缓存数量即将到达容量上线时，则会进行缓存回收，回收最近没有使用或总体上很少使用的缓存项。
     * 需要注意的是在接近这个容量上限时就会发生，所以在定义这个值的时候需要视情况适量地增大一点。
     * 另外通过expireAfterWrite这个方法定义了缓存的过期时间，写入十分钟之后过期。
     * 在build方法里，传入了一个CacheLoader对象，重写了其中的load方法。当获取的缓存值不存在或已过期时，则会调用此load方法，进行缓存值的计算。
     * 这就是最简单也是我们平常最常用的一种使用方法。定义了缓存大小、过期时间及缓存值生成方法。
     * <p>
     * 如果用其他的缓存方式，如redis，我们知道上面这种“如果有缓存则返回；否则运算、缓存、然后返回”的缓存模式是有很大弊端的。
     * 当高并发条件下同时进行get操作，而此时缓存值已过期时，会导致大量线程都调用生成缓存值的方法，比如从数据库读取。这时候就容易造成数据库雪崩。
     * 这也就是我们常说的“缓存穿透”。
     * 而Guava cache则对此种情况有一定控制。当大量线程用相同的key获取缓存值时，只会有一个线程进入load方法，而其他线程则等待，直到缓存值被生成。
     * 这样也就避免了缓存穿透的危险。
     */
//    LoadingCache<String, Object> caches = CacheBuilder.newBuilder()
//            .maximumSize(100)
//            .expireAfterWrite(10, TimeUnit.MINUTES)
//            .build(new CacheLoader<String, Object>() {
//                @Override
//                public Object load(String key) throws Exception {
//                    return generateValueByKey(key);
//                }
//            });


    /**
     * 定时刷新
     *
     * 虽然不会有缓存穿透的情况，但是每当某个缓存值过期时，老是会导致大量的请求线程被阻塞。
     * 而Guava则提供了另一种缓存策略，缓存值定时刷新：更新线程调用load方法更新该缓存，其他请求线程返回该缓存的旧值。
     * 这样对于某个key的缓存来说，只会有一个线程被阻塞，用来生成缓存值，而其他的线程都返回旧的缓存值，不会被阻塞。
     * 这里就需要用到Guava cache的refreshAfterWrite方法。如下所示：
     */
//    LoadingCache<String, Object> caches = CacheBuilder.newBuilder()
//            .maximumSize(100)
//            .refreshAfterWrite(10, TimeUnit.MINUTES)
//            .build(new CacheLoader<String, Object>() {
//                @Override public Object load(String key) throws Exception {
//                    return generateValueByKey(key);
//                }
//            });

    /**
     * 异步刷新
     * 解决了同一个key的缓存过期时会让多个线程阻塞的问题，只会让用来执行刷新缓存操作的一个用户线程会被阻塞。
     * 由此可以想到另一个问题，当缓存的key很多时，高并发条件下大量线程同时获取不同key对应的缓存，
     * 此时依然会造成大量线程阻塞，并且给数据库带来很大压力。这个问题的解决办法就是将刷新缓存值的任务交给后台线程，
     * 所有的用户请求线程均返回旧的缓存值，这样就不会有用户线程被阻塞了。
     */
//    ListeningExecutorService backgroundRefreshPools = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(20));
//    LoadingCache<String, Object> caches = CacheBuilder.newBuilder().maximumSize(100).refreshAfterWrite(10, TimeUnit.MINUTES).build(new CacheLoader<String, Object>() {
//        @Override
//        public Object load(String key) throws Exception {
//            return generateValueByKey(key);
//        }
//
//        @Override
//        public ListenableFuture<Object> reload(String key, Object oldValue) throws Exception {
//            return backgroundRefreshPools.submit(new Callable<Object>() {
//                @Override
//                public Object call() throws Exception {
//                    return generateValueByKey(key);
//                }
//            });
//        }
//    });

    /**
     * 可以看到防缓存穿透和防用户线程阻塞都是依靠返回旧值来完成的。所以如果没有旧值，同样会全部阻塞，
     * 因此应视情况尽量在系统启动时将缓存内容加载到内存中。
     * 在刷新缓存时，如果generateValueByKey方法出现异常或者返回了null，此时旧值不会更新。
     * 题外话：在使用内存缓存时，切记拿到缓存值之后不要在业务代码中对缓存直接做修改，因为此时拿到的对象引用是指向缓存真正的内容的。
     * 如果需要直接在该对象上进行修改，则在获取到缓存值后拷贝一份副本，然后传递该副本，进行修改操作。（我曾经就犯过这个低级错误 - -！）
     */
}
