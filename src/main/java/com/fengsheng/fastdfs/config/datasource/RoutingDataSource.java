package com.fengsheng.fastdfs.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

/**
 * 数据源切换器
 */
@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        Object key = DBContextHolder.get();
        log.debug("动态数据源 dataSourceKey = {}", key);
        return key;
    }
}
