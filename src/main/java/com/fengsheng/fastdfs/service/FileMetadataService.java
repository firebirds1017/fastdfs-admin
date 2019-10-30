package com.fengsheng.fastdfs.service;

import com.fengsheng.fastdfs.config.datasource.Master;
import com.fengsheng.fastdfs.dao.FileMetadataDao;
import com.fengsheng.fastdfs.domain.FileMetadataDomain;
import com.fengsheng.fastdfs.domain.PageResponse;
import com.fengsheng.fastdfs.entity.FileMetadata;
import com.fengsheng.fastdfs.utils.FastDFSClientUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Log4j2(topic = "com.fengsheng.fastdfs")
@Transactional(readOnly = true, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
public class FileMetadataService {

    @Autowired
    private FileMetadataDao fileMetadataDao;


    /**
     * 图片上传
     *
     * @param domain
     * @return
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    @Master
    public FileMetadataDomain uploadImg(FileMetadataDomain domain) throws Exception {

        FileMetadata fileMetadata = fileMetadataDao.getByUrlPathOrOriginalName(domain.getUrlPath(), domain.getOriginalName());
        log.debug(fileMetadata);
        String fileUrl;
        if (fileMetadata == null || fileMetadata.getId() == null) {
            //新增
            fileMetadata = new FileMetadata();
            fileUrl = FastDFSClientUtils.uploadBase64(domain.getBase64Img(), domain.getFileExt(), domain.getBusinessID());
            log.debug("new file :" + fileUrl + ",original name: " + domain.getOriginalName() + ",business id :" + domain.getBusinessID());
        } else {
            //修改
            fileUrl = FastDFSClientUtils.
                    modifyBase64(fileMetadata.getDfsGroup(), fileMetadata.getUrlPath()
                            , domain.getBase64Img(), domain.getFileExt(), domain.getBusinessID());
            log.debug("modify file :" + fileUrl + ",original name: " + domain.getOriginalName() +
                    ",business id :" + domain.getBusinessID() + ",old file:" + domain.getUrlPath());
        }

        if (fileUrl == null) return null;
        domain.setUrlPath(fileUrl);
        domain.setDfsGroup(getGroup(fileUrl));
        convertToDb(domain, fileMetadata);
        FileMetadata newFile = fileMetadataDao.save(fileMetadata);
        if (newFile.getId() == null) return null;
        return convertToDomain(newFile);
    }

    private String getGroup(String fileUrl) {
        if (fileUrl != null) {
            String[] result = fileUrl.split("/");
            return result[0];
        }
        return null;
    }

    private FileMetadataDomain convertToDomain(FileMetadata newFile) throws Exception {
        FileMetadataDomain domain = new FileMetadataDomain();
        domain.setId(newFile.getId());
        domain.setOriginalName(newFile.getOriginalName());
        domain.setCreateTime(newFile.getCreateTime());
        domain.setUrlPath(newFile.getUrlPath());
        domain.setUrlPath(FastDFSClientUtils.getTokenAndTimestamp(newFile.getUrlPath()));
        domain.setDfsGroup(newFile.getDfsGroup());
        domain.setFileExt(newFile.getFileExt());
        domain.setFileSize(newFile.getFileSize());
        return domain;
    }

    private FileMetadataDomain convertToAdminDomain(FileMetadata newFile) throws Exception {
        FileMetadataDomain domain = convertToDomain(newFile);
        domain.setReferrerUrl(newFile.getReferrerUrl());
        domain.setUploadIp(newFile.getUploadIp());
        domain.setBusinessID(newFile.getBusinessID());
        return domain;
    }

    private void convertToDb(FileMetadataDomain domain, FileMetadata db) {
        if (db.getId() != null) {
            db.setUpdateTime(new Date());

        } else {
            db.setCreateTime(new Date());
        }
        db.setReferrerUrl(domain.getReferrerUrl());
        db.setUploadIp(domain.getUploadIp());
        db.setBusinessID(domain.getBusinessID());
        db.setFileExt(domain.getFileExt());
        db.setFileSize(domain.getFileSize());
        db.setOriginalName(domain.getOriginalName());
        db.setDfsGroup(domain.getDfsGroup());
        db.setUrlPath(domain.getUrlPath());

    }

    /**
     * 查询文件
     *
     * @param domain
     * @return
     */
    public PageResponse<FileMetadataDomain> selectFiles(FileMetadataDomain domain) {
        PageResponse<FileMetadataDomain> response = new PageResponse<>();
        PageRequest request = PageRequest.of(domain.getPage(), domain.getPageSize(), Sort.by(domain.getOrderField()));
        //生成查询条件
        ExampleMatcher matcher = ExampleMatcher.matching().
                withMatcher("businessId", ExampleMatcher.GenericPropertyMatchers.contains())//全部模糊查询，即%{address}%
                .withMatcher("fileExt", ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher("originalName", ExampleMatcher.GenericPropertyMatchers.contains());
        FileMetadata fileMetadata = new FileMetadata();
        fileMetadata.setOriginalName(domain.getOriginalName());
        fileMetadata.setFileExt(domain.getFileExt());
        fileMetadata.setBusinessID(domain.getBusinessID());
        Example<FileMetadata> example = Example.of(fileMetadata, matcher);
        //按条件查询
        Page<FileMetadata> fileMetadataPage = fileMetadataDao.findAll(example, request);
        //如果没有查到直接返回
        if (fileMetadataPage.isEmpty()) {
            return response;
        }
        //处理返回数据
        List<FileMetadata> fileMetadataList = fileMetadataPage.getContent();
        List<FileMetadataDomain> domainList = new ArrayList<>();
        fileMetadataList.forEach(d -> {
            try {
                domainList.add(convertToDomain(d));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        response.setCount(fileMetadataPage.getTotalElements());
        response.setData(domainList);

        return response;
    }

    /**
     * 删除文件,只能一张一张删除
     *
     * @param fileUrls
     * @return
     */
    @Master
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int deleteFilesByPaths(List<String> fileUrls) {

        List<FileMetadata> fileMetadataList = fileMetadataDao.findByUrlPathIn(fileUrls);

        return deleteFiles(fileMetadataList);
    }

    @Master
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int deleteFilesByIds(List<String> ids) {

        List<FileMetadata> fileMetadataList = fileMetadataDao.findAllById(ids);

        return deleteFiles(fileMetadataList);
    }

    private int deleteFiles(List<FileMetadata> fileMetadataList) {
        int result = 0;
        if (fileMetadataList != null && !fileMetadataList.isEmpty()) {
            for (FileMetadata fileMetadata : fileMetadataList) {
                result += FastDFSClientUtils.delete(fileMetadata.getDfsGroup(), fileMetadata.getUrlPath());
            }
        } else {
            result = 1;
        }
        fileMetadataDao.deleteInBatch(fileMetadataList);

        return result;
    }

    /**
     * 通过id；列表获取文件元数据
     *
     * @param ids
     * @return
     */
    public List<FileMetadataDomain> getFilesByIds(List<String> ids) {
        List<FileMetadata> fileMetadataList = fileMetadataDao.findAllById(ids);
        List<FileMetadataDomain> domainList = new ArrayList<>();
        fileMetadataList.forEach(d -> {
            try {
                domainList.add(convertToDomain(d));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return domainList;
    }

    public FileMetadataDomain getFileById(String imageId) throws Exception {
        FileMetadata fileMetadata = fileMetadataDao.getOne(imageId);
        if (fileMetadata.getId()!=null){
            return convertToDomain(fileMetadata);
        }
        return null;
    }
}
