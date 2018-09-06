package learn.sharding.jdbc.example.conf;

import com.alibaba.druid.pool.DruidDataSource;
import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import learn.sharding.jdbc.example.algorithm.SingleKeyModuloDatabaseShardingAlgorithm;
import learn.sharding.jdbc.example.algorithm.SingleKeyModuloDatabaseShardingAlgorithm2;
import learn.sharding.jdbc.example.algorithm.SingleKeyModuloTableShardingAlgorithm;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(ShardDataSourceProperties.class)
public class ShardDataSourceConfig {

    @Autowired
    private ShardDataSourceProperties shardDataSourceProperties;


    private DruidDataSource parentDs() throws SQLException {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(shardDataSourceProperties.getDriverClassName());
        ds.setUsername(shardDataSourceProperties.getUsername());
        ds.setUrl(shardDataSourceProperties.getUrl());
        ds.setPassword(shardDataSourceProperties.getPassword());
        ds.setFilters(shardDataSourceProperties.getFilters());
        ds.setMaxActive(shardDataSourceProperties.getMaxActive());
        ds.setInitialSize(shardDataSourceProperties.getInitialSize());
        ds.setMaxWait(shardDataSourceProperties.getMaxWait());
        ds.setMinIdle(shardDataSourceProperties.getMinIdle());
        ds.setTimeBetweenEvictionRunsMillis(shardDataSourceProperties.getTimeBetweenEvictionRunsMillis());
        ds.setMinEvictableIdleTimeMillis(shardDataSourceProperties.getMinEvictableIdleTimeMillis());
        ds.setValidationQuery(shardDataSourceProperties.getValidationQuery());
        ds.setTestWhileIdle(shardDataSourceProperties.isTestWhileIdle());
        ds.setTestOnBorrow(shardDataSourceProperties.isTestOnBorrow());
        ds.setTestOnReturn(shardDataSourceProperties.isTestOnReturn());
        ds.setPoolPreparedStatements(shardDataSourceProperties.isPoolPreparedStatements());
        ds.setMaxPoolPreparedStatementPerConnectionSize(
                shardDataSourceProperties.getMaxPoolPreparedStatementPerConnectionSize());
        ds.setRemoveAbandoned(shardDataSourceProperties.isRemoveAbandoned());
        ds.setRemoveAbandonedTimeout(shardDataSourceProperties.getRemoveAbandonedTimeout());
        ds.setLogAbandoned(shardDataSourceProperties.isLogAbandoned());
        ds.setConnectionInitSqls(shardDataSourceProperties.getConnectionInitSqls());
        return ds;
    }

    //加载数据源
    private DataSource ds_0() throws SQLException {
        DruidDataSource ds_0 = parentDs();
        ds_0.setUrl(shardDataSourceProperties.getUrl0());
        ds_0.setUsername(shardDataSourceProperties.getUsername0());
        ds_0.setPassword(shardDataSourceProperties.getPassword0());
        return ds_0;
    }

    private DataSource ds_1() throws SQLException {
        DruidDataSource ds_1 = parentDs();
        ds_1.setUrl(shardDataSourceProperties.getUrl1());
        ds_1.setUsername(shardDataSourceProperties.getUsername1());
        ds_1.setPassword(shardDataSourceProperties.getPassword1());
        return ds_1;
    }

    //配置数据源规则
    private DataSourceRule dataSourceRule() throws SQLException {
        Map<String, DataSource> dataSourceMap = new HashMap<>(2);
        dataSourceMap.put("ds_0", ds_0());
        dataSourceMap.put("ds_1", ds_1());
        DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap);
        return dataSourceRule;
    }

    /**
     * 设置表规则：第一个参数是逻辑表，第二个参数是实际分表
     *
     * @return
     */
    private TableRule orderTableRule() throws SQLException {
        TableRule orderTableRule = TableRule.builder("t_order").actualTables(Arrays.asList("t_order_0", "t_order_1"))
                .dataSourceRule(dataSourceRule()).build();
        return orderTableRule;
    }

    private TableRule orderItemTableRule() throws SQLException {
        TableRule orderItemTableRule = TableRule.builder("t_order_item").actualTables(Arrays.asList("t_order_item_0",
                "t_order_item_1")).dataSourceRule(dataSourceRule()).build();
        return orderItemTableRule;
    }

    /**
     * 设置分片规则
     *
     * @return
     * @throws SQLException
     */
    private ShardingRule shardingRule() throws SQLException {
        ShardingRule shardingRule = ShardingRule.builder().dataSourceRule(dataSourceRule()).tableRules(Arrays.asList
                (orderItemTableRule(), orderTableRule()))
                //.databaseShardingStrategy(new DatabaseShardingStrategy("user_id", new SingleKeyModuloDatabaseShardingAlgorithm()))
                .databaseShardingStrategy(new DatabaseShardingStrategy("city", new SingleKeyModuloDatabaseShardingAlgorithm2()))
                .tableShardingStrategy(new TableShardingStrategy("order_id", new SingleKeyModuloTableShardingAlgorithm())).build();
        return shardingRule;
    }

    /**
     * @return
     * @throws SQLException
     */
    @Bean
    public DataSource dataSource() throws SQLException {
        return ShardingDataSourceFactory.createDataSource(shardingRule());
    }

    /**
     * 配置mybatis
     *
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        /*PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:/mybatis*//*.xml"));*/
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 配置事务管理器
     *
     * @return
     * @throws SQLException
     */
    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {
        return new DataSourceTransactionManager(dataSource());
    }
}
