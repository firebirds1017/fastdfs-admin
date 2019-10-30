package com.fengsheng.fastdfs.controller;

import com.fengsheng.fastdfs.domain.FileMetadataDomain;
import com.fengsheng.fastdfs.domain.PageResponse;
import com.fengsheng.fastdfs.domain.Result;
import com.fengsheng.fastdfs.domain.UploadImgFile;
import com.fengsheng.fastdfs.service.FileMetadataService;
import com.fengsheng.fastdfs.utils.ImageUploadUtils;
import com.fengsheng.fastdfs.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

/**
 * 需要验证referer，只有指定的referer可以访问
 */
@RestController
@RequestMapping("service/staticFile")
public class ManageFastDfsController extends BaseController<FileMetadataDomain> {

    @Autowired
    private FileMetadataService fileMetadataService;

    /**
     * 上传照片和修改照片,携带原始路径就是修改，否则为添加
     *
     * @return
     */
    @PostMapping("uploadImg")
    public Result<FileMetadataDomain> uploadImg(@RequestBody UploadImgFile imgFile, HttpServletRequest request) throws Exception {
        Result<FileMetadataDomain> result = new Result<>();
        if (validateImgFile(imgFile, result)) return result;
        FileMetadataDomain domain = getFileMetadataDomain(imgFile, request);
        FileMetadataDomain uploadResult = fileMetadataService.uploadImg(domain);
        return getResult(result, uploadResult);
    }


    private FileMetadataDomain getFileMetadataDomain(@RequestBody UploadImgFile imgFile, HttpServletRequest request) {
        FileMetadataDomain domain = new FileMetadataDomain();
        Base64.Decoder decoder = Base64.getDecoder();
        domain.setUploadIp(IpUtils.getClientIpAddress(request));
        byte[] bytes1 = decoder.decode(imgFile.getBase64Img());
        domain.setFileSize(bytes1.length);
        domain.setReferrerUrl(request.getHeader("referer"));
        domain.setBusinessID(imgFile.getBusinessId());
        domain.setOriginalName(imgFile.getFileName());
        domain.setBase64Img(imgFile.getBase64Img());
        domain.setFileExt("jpg");
        domain.setUrlPath(imgFile.getFileId());
        return domain;
    }

    /**
     * 分页查询，page从0开始
     *
     * @param domain
     * @return
     */

    @PostMapping("page")
    public PageResponse<FileMetadataDomain> selectFilesPage(@RequestBody(required = false) FileMetadataDomain domain) {
        return fileMetadataService.selectFiles(domain);
    }

    @PostMapping("getFiles")
    public List<FileMetadataDomain> getFiles(@RequestBody List<String> ids) {

        return fileMetadataService.getFilesByIds(ids);
    }

    @PostMapping("getFile")
    public Object getFile(@RequestBody String imageId) throws Exception {
        FileMetadataDomain domain = fileMetadataService.getFileById(imageId);
        if (domain == null) {
            return new Object();
        }
        return domain;
    }

    /**
     * 删除文件
     *
     * @param fileUrls
     * @return
     */
    @PostMapping("delByPaths")
    public Result<FileMetadataDomain> deleteFileByPaths(@RequestBody List<String> fileUrls) {
        Result<FileMetadataDomain> result = new Result<>();
        int deleteResult = fileMetadataService.deleteFilesByPaths(fileUrls);
        return getResult(result, deleteResult);
    }

    @PostMapping("del")
    public Result<FileMetadataDomain> deleteFile(@RequestBody List<String> fileUrls) {
        Result<FileMetadataDomain> result = new Result<>();
        int deleteResult = fileMetadataService.deleteFilesByIds(fileUrls);
        return getResult(result, deleteResult);
    }

    private boolean validateImgFile(@RequestBody UploadImgFile imgFile, Result<FileMetadataDomain> result) throws IOException {
        InputStream inputStream = ImageUploadUtils.baseToInputStream(imgFile.getBase64Img());
        if (inputStream == null) {
            result.setCode("-1");
            result.setMsg("上传的文件不能为空");
            return true;
        }
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        if (bufferedImage == null) {
            result.setCode("-1");
            result.setMsg("无效的图片");
            return true;
        }
        return false;
    }


}
