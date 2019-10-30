package com.fengsheng.fastdfs.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FS_FILE_METADATA")
@Data
@DynamicUpdate
@Cacheable(value = "fastdfs")
public class FileMetadata {

    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    @GeneratedValue(generator = "systemUUID")
    private String id;

    @Column(name = "ORIGINAL_NAME",unique = true)
    private String originalName;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    @Column(name = "FILE_EXT")
    private String fileExt;

    @Column(name = "URL_PATH",unique = true)
    private String urlPath;

    @Column(name = "DFS_GROUP")
    private String dfsGroup;

    @Column(name = "FILE_SIZE")
    private Integer fileSize;

    @Column(name = "REFERRER_URL")
    private String referrerUrl;

    @Column(name = "UPLOAD_IP")
    private String uploadIp;

    @Column(name = "BUSINESS_ID")
    private String businessID;

}
