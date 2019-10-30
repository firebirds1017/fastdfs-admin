package com.fengsheng.fastdfs;

import com.fengsheng.fastdfs.utils.FastDFSClientUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class FastdfsAdminTest {

    @Before
    public void initFastDfs() throws MyException ,IOException{
        ClientGlobal.initByProperties("fastdfs-client.properties");
    }

    @Test
    public void getFastInfo(){
        System.out.println("ClientGlobal.configInfo(): " + ClientGlobal.configInfo());
        try {
            TrackerClient tracker = new TrackerClient();
            TrackerServer trackerServer = tracker.getConnection();
            StorageServer storageServer = tracker.getStoreStorage(trackerServer);
            StorageClient storageClient = new StorageClient(trackerServer, storageServer);
//            FileInfo fi = storageClient.get_file_info("group1/M00/00/00/wKgAQ1z4cdKAY5iTAA_AmbTMxrM.tar.gz");
//            System.out.println(fi.getSourceIpAddr());
//            System.out.println(fi.getFileSize());
//            System.out.println(fi.getCreateTimestamp());
//            System.out.println(fi.getCrc32());
          String[] path=  storageClient.upload_file("E:/index.jpg","jpg",null);
          System.out.println(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void uploadFile() throws  Exception{
        String client = FastDFSClientUtils.upload("E:/index.jpg");

      System.out.println("upload success: "+client);
    }

    @Test
    public void getInfo() throws  Exception{
        FileInfo client = FastDFSClientUtils.getFile("group1","group1/M00/00/00/wKgARVz-BrKAXbCLAADPIRyf2s8758.jpg");

        System.out.println("upload success: "+client);
    }

    @Test
    public void deleteFile() throws  Exception{
        int client = FastDFSClientUtils.delete("group1","group1/M00/00/00/wKhQC10i5RqAC4ZeAAAURmQnPmg327.jpg");

        System.out.println("upload success: "+client);
    }

    @Test
    public void testGroup(){
        String urlPath = "group1/M00/00/00/wKgAyFz-AaOAJYtaAA_AmbTMxrM.tar.gz";
        String[] result = urlPath.split("/");
     System.out.println(result[0]);
    }
}
