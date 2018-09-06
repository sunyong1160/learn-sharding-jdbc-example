package learn.sharding.jdbc.example.algorithm;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sunyong on 2018-09-06.
 */
public class SingleKeyModuloDatabaseShardingAlgorithm2 implements SingleKeyDatabaseShardingAlgorithm<String> {

    private static Map<String, List<String>> shardingMap = new ConcurrentHashMap<>();

    static {
        shardingMap.put("ds_0", Arrays.asList("上海", "合肥"));
        shardingMap.put("ds_1", Arrays.asList("北京", "南京"));
    }

    @Override
    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<String> shardingValue) {
        for (String each : availableTargetNames) {
            if (shardingMap.get(each).contains(shardingValue.getValue())) {
                return each;
            }
        }
        return "ds_0";//不满足的时候
    }


    @Override
    public Collection<String> doInSharding(Collection<String> availableTargetNames, ShardingValue<String>
            shardingValue) {
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        for (String each : availableTargetNames) {
            if (shardingMap.get(each).contains(shardingValue.getValue())) {
                result.add(each);
            } else {
                result.add("ds_0");
            }
        }
        return result;
    }


    @Override
    public Collection<String> doBetweenSharding(Collection<String> availableTargetNames, ShardingValue<String>
            shardingValue) {
        Collection<String> result = new LinkedHashSet<>(availableTargetNames.size());
        for (String each : availableTargetNames) {
            if (shardingMap.get(each).contains(shardingValue.getValue())) {
                result.add(each);
            } else {
                result.add("ds_0");
            }
        }
        return result;
    }
}
