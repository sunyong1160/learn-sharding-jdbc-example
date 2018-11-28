package learn.sharding.jdbc.example.guavacache;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import learn.sharding.jdbc.example.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Component
@Slf4j
public class StudentCache {

    private final String VERSION = "version";

    private Map<String, Student> configMap = new ConcurrentHashMap<>();

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * 10秒钟更新一次
     */
    private LoadingCache<String, Optional<Object>> cache = CacheBuilder
            .newBuilder()
            .refreshAfterWrite(50, TimeUnit.SECONDS)
            .maximumSize(10)
            .build(new CacheLoader<String, Optional<Object>>() {
                @Override
                public Optional<Object> load(String key) throws Exception {
                    log.info("guava  load by 定时加载配置至内存" + DateUtil.format(LocalDateTime.now()));
                    getAll();
                    return Optional.fromNullable(VERSION);
                }

                @Override
                public ListenableFuture<Optional<Object>> reload(String key, Optional<Object> oldValue) throws Exception {
                    log.info(" guava reload by 定时加载配置至内存" + DateUtil.format(LocalDateTime.now()));
                    getAllNew();
                    return Futures.immediateFuture(Optional.fromNullable(VERSION));
                }
            });

    private void getAllNew() {
        System.out.println("@@@@@@@@@@@@@@@@");
        List<Student> list = new ArrayList<>();
        for (int i = 1; i < 18; i++) {
            list.add(new Student(i + "", "name__" + i));
        }
        list.stream().filter(t -> t != null).forEach(t -> {
            configMap.put(t.getId(), t);
        });
        System.out.println("$$$$$$$$$$$$$$$$");
    }

    private void getAll() {
        System.out.println("=================");
        List<Student> list = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            list.add(new Student(i + "", "name_" + i));
        }
        list.stream().filter(t -> t != null).forEach(t -> {
            configMap.put(t.getId(), t);
        });
        System.out.println("#################");
    }


    public Student getConfig(String key) {
        // 检查是否需要定时更新内容
        cache.getUnchecked(VERSION).get();
        System.out.println("cache_size：" + cache.size());
        System.out.println("configMap_size：" + configMap.size());
        return configMap.get(key);
    }

}
