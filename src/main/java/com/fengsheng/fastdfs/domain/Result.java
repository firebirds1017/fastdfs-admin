package com.fengsheng.fastdfs.domain;

import lombok.Data;

@Data
public class Result<T> {

    /** 错误代码. */
    private  String code;

    /** 提示信息. */
    private String msg;

    /** 具体内容. */
    private T data;

}
