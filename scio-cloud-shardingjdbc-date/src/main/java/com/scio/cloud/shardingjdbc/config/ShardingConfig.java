package com.scio.cloud.shardingjdbc.config;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.map.LazyMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.HintShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.ShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.api.sharding.hint.HintShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.hint.HintShardingValue;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.google.common.collect.Lists;
import com.scio.cloud.shardingjdbc.util.DataSourceUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ShardingConfig {
    static Date startShardingMonth;
    static {
        try {
            startShardingMonth = DateUtils.parseDate("20200201", "yyyyMMdd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // @Bean
    public DataSource getDataSource() {
        return DataSourceUtil.createDataSource("loanapp");
    }

    @Bean
    @Primary
    public DataSource shardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(getEAVEntityTableRule());
        shardingRuleConfig.getTableRuleConfigs().add(getEAVValueTableRule());

        shardingRuleConfig.getBindingTableGroups().add("eav_entity, eav_value");

        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(getDataBaseShardingStrategy());
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(getTableShardingStrategy());
        Properties p = new Properties();
        p.setProperty("sql.show", "true");
        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, p);
    }

    public TableRuleConfiguration getEAVEntityTableRule() {
        TableRuleConfiguration rule =
            new TableRuleConfiguration("eav_entity", "loanapp.eav_entity, loanapp.eav_entity,loanappnew.eav_entity");
        rule.setDatabaseShardingStrategyConfig(getDataBaseShardingStrategy());
        rule.setTableShardingStrategyConfig(getTableShardingStrategy());
        return rule;
    }

    public TableRuleConfiguration getEAVValueTableRule() {
        TableRuleConfiguration rule = new TableRuleConfiguration("eav_value", "loanapp.eav_value,loanappnew.eav_value");
        rule.setDatabaseShardingStrategyConfig(getHintDataBaseShardingStrategy());
        rule.setTableShardingStrategyConfig(getHintTableShardingStrategy());
        return rule;
    }

    public ShardingStrategyConfiguration getHintTableShardingStrategy() {
        return new HintShardingStrategyConfiguration(new HintShardingAlgorithm<Comparable<String>>() {
            @Override
            public Collection<String> doSharding(Collection<String> availableTargetNames,
                HintShardingValue<Comparable<String>> shardingValue) {
                log.info(" EntityId table sharding {},{}", availableTargetNames, shardingValue);
                String loanAppCode = (String)shardingValue.getValues().iterator().next();
                String month = StringUtils.substring(loanAppCode, 4, 10);
                log.info(" EntityId loanAppCode in month:{}", month);
                Date monthDate = new Date();
                try {
                    monthDate = DateUtils.parseDate(month, "yyyyMM");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String tableName = shardingValue.getLogicTableName();
                if (monthDate.compareTo(startShardingMonth) >= 0) {
                    // 需要走分片
                    tableName = shardingValue.getLogicTableName().concat("_").concat(month);
                } else {
                    // 不需要走分片
                    tableName = shardingValue.getLogicTableName();
                }
                return Lists.newArrayList(tableName);
            }
        });
    }

    public ShardingStrategyConfiguration getHintDataBaseShardingStrategy() {

        return new HintShardingStrategyConfiguration(new HintShardingAlgorithm<Comparable<String>>() {
            @Override
            public Collection<String> doSharding(Collection<String> availableTargetNames,
                HintShardingValue<Comparable<String>> shardingValue) {
                log.info(" database sharding {},{}", availableTargetNames, shardingValue);

                String loanAppCode = (String)shardingValue.getValues().iterator().next();
                String month = StringUtils.substring(loanAppCode, 4, 10);

                if (month.compareTo("202001") >= 0) {
                    return Lists.newArrayList("loanappnew");
                } else {
                    return Lists.newArrayList("loanapp");
                }
            }
        });
    }

    public ShardingStrategyConfiguration getDataBaseShardingStrategy() {
        return new StandardShardingStrategyConfiguration("loan_app_code",
            new PreciseShardingAlgorithm<Comparable<String>>() {
                @Override
                public String doSharding(Collection<String> availableTargetNames,
                    PreciseShardingValue<Comparable<String>> shardingValue) {
                    log.info(" database sharding {},{}", availableTargetNames, shardingValue);

                    String loanAppCode = (String)shardingValue.getValue();
                    String month = StringUtils.substring(loanAppCode, 4, 10);

                    if (month.compareTo("202001") >= 0) {
                        return "loanappnew";
                    } else {
                        return "loanapp";
                    }
                }
            });
    }

    public ShardingStrategyConfiguration getTableShardingStrategy() {
        return new StandardShardingStrategyConfiguration("loan_app_code",
            new PreciseShardingAlgorithm<Comparable<String>>() {
                @Override
                public String doSharding(Collection<String> availableTargetNames,
                    PreciseShardingValue<Comparable<String>> shardingValue) {
                    log.info(" table sharding {},{}", availableTargetNames, shardingValue);
                    String loanAppCode = (String)shardingValue.getValue();
                    String month = StringUtils.substring(loanAppCode, 4, 10);
                    log.info(" loanAppCode in month:{}", month);
                    Date monthDate = new Date();
                    try {
                        monthDate = DateUtils.parseDate(month, "yyyyMM");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String tableName = shardingValue.getLogicTableName();
                    if (monthDate.compareTo(startShardingMonth) >= 0) {
                        // 需要走分片
                        tableName = shardingValue.getLogicTableName().concat("_").concat(month);
                    } else {
                        // 不需要走分片
                        tableName = shardingValue.getLogicTableName();
                    }
                    return tableName;
                }
            });
    }

    Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>();
        result.put("loanapp", DataSourceUtil.createDataSource("loanapp"));
        //result.put("loanappnew", DataSourceUtil.createDataSource("loanappnew"));
        result = LazyMap.lazyMap(result, new Transformer() {
            @Override
            public Object transform(Object input) {
                return DataSourceUtil.createDataSource((String)input);
            }
        });
        return result;
    }
}
