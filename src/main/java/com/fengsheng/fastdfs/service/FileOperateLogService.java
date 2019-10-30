package com.fengsheng.fastdfs.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true,rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
public class FileOperateLogService {


}
