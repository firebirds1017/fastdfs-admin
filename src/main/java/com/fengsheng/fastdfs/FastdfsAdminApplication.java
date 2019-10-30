package com.fengsheng.fastdfs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.fengsheng.fastdfs.dao")
@ServletComponentScan("com.fengsheng.fastdfs.filter")
public class FastdfsAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastdfsAdminApplication.class, args);
    }

}
