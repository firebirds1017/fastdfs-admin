package com.fengsheng.fastdfs.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * slf4j日志写到数据库的连接工厂类
 */
@Component
public class Log4j2ConnectionFactory implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }
    public static Connection getDruidConnection() throws SQLException {
        DataSource dataSource = (DataSource)getBean("writeDataSource");
        return dataSource.getConnection();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Log4j2ConnectionFactory.applicationContext = applicationContext;
    }
}
