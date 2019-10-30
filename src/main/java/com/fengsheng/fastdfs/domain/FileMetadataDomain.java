package com.fengsheng.fastdfs.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

@Data
public class FileMetadataDomain {

    private String id;

    private String originalName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String fileExt;

    private String urlPath;

    private String dfsGroup;

    private int fileSize;

    private String referrerUrl;

    private String uploadIp;

    private String businessID;

    private String base64Img;

    private int page;// 页码

    private int pageSize; // 每页数据行数


   public FileMetadataDomain(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }
    public FileMetadataDomain() {
    }

    private String orderField = "createTime"; // 排序字段

    private String orderType = "desc"; // 排序类型 asc ,desc
}
