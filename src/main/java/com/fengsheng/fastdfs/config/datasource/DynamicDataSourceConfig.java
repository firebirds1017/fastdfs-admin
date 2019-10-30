package com.fengsheng.fastdfs.config.datasource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


@Configuration
@ServletComponentScan
@ConfigurationProperties(prefix = "spring.datasource.druid")
public class DynamicDataSourceConfig {

    //注入druid连接池
    @Value("${spring.datasource.type}")
    private Class<? extends DataSource> dataSourceType;

    //读写数据源
    @Bean(name = "writeDataSource", destroyMethod = "close", initMethod = "init")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.druid.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    //只读数据源
    @Bean(name = "readDataSource", destroyMethod = "close", initMethod = "init")
    @ConfigurationProperties(prefix = "spring.datasource.druid.slave")
    public DataSource slaveDataSource() {
        return DataSourceBuilder.create().type(dataSourceType).build();
    }

    //路由数据源
    @Bean
    public DataSource myRoutingDataSource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBTypeEnum.MASTER, masterDataSource());
        targetDataSources.put(DBTypeEnum.SLAVE, slaveDataSource());
        RoutingDataSource myRoutingDataSource = new RoutingDataSource();
        myRoutingDataSource.setDefaultTargetDataSource(masterDataSource());
        myRoutingDataSource.setTargetDataSources(targetDataSources);
        return myRoutingDataSource;
    }

    //扫描实体类
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean emf() {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(myRoutingDataSource()); //这里一定要是你自己App实体类的位置
        emf.setPackagesToScan(new String[]{"com.fengsheng.fastdfs.entity"});
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return emf;
    }

    /**
     * 事务关管理 * @return
     */
    @Bean(name = "transactionManager")
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager tm = new JpaTransactionManager();
        EntityManagerFactory e = emf().getNativeEntityManagerFactory();
        tm.setEntityManagerFactory(e);
        return tm;
    }

}
