package com.fengsheng.fastdfs.domain;

import lombok.Data;

@Data
public class UploadImgFile {
    private String fileName;  //原始文件名
    private String base64Img; //图片的base64编码
    private String businessId; //业务信息
    private String fileId; //在fastDfs上面的路径

}
