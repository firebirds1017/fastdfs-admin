package com.fengsheng.fastdfs.dao;

import com.fengsheng.fastdfs.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FileOperateDao extends JpaRepository<FileMetadata,Long>, JpaSpecificationExecutor<FileMetadata> {
}
