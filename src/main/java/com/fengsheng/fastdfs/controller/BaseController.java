package com.fengsheng.fastdfs.controller;

import com.fengsheng.fastdfs.domain.Result;

public class BaseController<T> {

    protected Result<T> getResult(Result<T> result, T data) {
        if (data != null) {
            result.setCode("0");
            result.setMsg("操作成功");
            result.setData(data);
        } else {
            result.setCode("-1");
            result.setMsg("操作失败");
        }
        return result;
    }

    protected Result<T> getResult(Result<T> result, int validCount) {

        if (validCount==0) {
            result.setCode("0");
            result.setMsg("操作成功");
        } else {
            result.setCode("-1");
            result.setMsg("操作失败");
        }
        return result;
    }
}
