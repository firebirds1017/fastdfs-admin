package com.fengsheng.fastdfs.dao;

import com.fengsheng.fastdfs.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileMetadataDao  extends JpaRepository<FileMetadata,String>, JpaSpecificationExecutor<FileMetadata> {

    FileMetadata getByUrlPathOrOriginalName(String urlPath,String originalName);

    FileMetadata getByUrlPath(String fileId);

    List<FileMetadata> findByUrlPathIn(List<String> asList);
}
