package com.fengsheng.fastdfs.entity;


import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FS_FILE_OPERATE_LOG")
@Data
@DynamicUpdate
@Cacheable(value = "fastdfs")
public class FileOperateLog {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "CREATE_TIME")
    private Date createTime;//'创建时间'

    @Column(name = "LEVEL")
    private String level;//'优先级'

    @Column(name = "CATEGORY")
    private String category;   // '所在类的全名'

    @Column(name = "FILE_NAME")
    private String fileName;//输出日志消息产生时所在的文件名称

    @Column(name = "THREAD_NAME")
    private String threadName;//线程名

    @Column(name = "LINE")
    private Integer line;// 行号

    @Column(name = "ALL_CATEGORY")
    private String allCategory;//日志事件的发生位置

    @Column(name = "MESSAGE")
    private String message;//输出代码中指定的消息

    @Column(name = "USER_IP")
    private String userIp;

    @Column(name = "REFERER")
    private String referer;
}
