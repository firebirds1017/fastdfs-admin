package com.fengsheng.fastdfs.utils;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.*;
import java.util.Base64;
import java.util.logging.Logger;

public class FastDFSClientUtils {
    private static final String CONF_FILENAME = "fastdfs-client.properties";

    private static Logger logger = Logger.getLogger(FastDFSClientUtils.class.getName());


    private static TrackerClient trackerClient;


    //加载文件
    static {
        try {
            ClientGlobal.initByProperties(CONF_FILENAME);
            TrackerGroup trackerGroup = ClientGlobal.g_tracker_group;
            trackerClient = new TrackerClient(trackerGroup);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }

    public static String getTokenAndTimestamp(String filePath) throws Exception {
        String[] results = new String[2];
        int errno = StorageClient1.split_file_id(filePath, results);
        if (errno != 0) {
            throw new MyException("fileId格式错误!");
        }
        int ts = (int) (System.currentTimeMillis() / 1000);
        String key = ClientGlobal.getG_secret_key();
        String token = ProtoCommon.getToken(results[1], ts, key);

        return filePath + "?token=" + token + "&ts=" + ts;

    }

    /**
     * <B>方法名称：</B>上传方法<BR>
     * <B>概要说明：</B><BR>
     *
     * @param file 文件
     * @param path 路径
     * @return 上传成功返回id，失败返回null
     */
    public static String upload(File file, String path, NameValuePair... meta_list) {
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        StorageClient1 storageClient1;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] file_buff = new byte[fis.available()];
            fis.read(file_buff);

            trackerServer = trackerClient.getConnection();
            if (trackerServer == null) {
                logger.info("getConnection return null");
            }
            storageServer = trackerClient.getStoreStorage(trackerServer, "group1");
            storageClient1 = new StorageClient1(trackerServer, storageServer);
            String fileid = storageClient1.upload_file1(file_buff, getFileExt(path), meta_list);

            return fileid;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    logger.info(e.getMessage());
                }
            }
            cleanConnection(trackerServer, storageServer);
        }
    }


    /**
     * 清理连接信息
     *
     * @param trackerServer Tracker服务器
     * @param storageServer Storage服务器
     */
    private static void cleanConnection(TrackerServer trackerServer, StorageServer storageServer) {
        if (storageServer != null) {
            try {
                storageServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (trackerServer != null) {
            try {
                trackerServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <B>方法名称：</B>上传方法<BR>
     * <B>概要说明：</B><BR>
     *
     * @param data    数据
     * @param extName 路径
     * @return 上传成功返回id，失败返回null
     */
    public static String upload(byte[] data, String extName, NameValuePair... meta_list) {
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        StorageClient1 storageClient1;
        try {

            trackerServer = trackerClient.getConnection();
            if (trackerServer == null) {
                logger.info("getConnection return null");
            }
            storageServer = trackerClient.getStoreStorage(trackerServer);
            storageClient1 = new StorageClient1(trackerServer, storageServer);
            String fileid = storageClient1.upload_file1(data, extName, meta_list);
            return fileid;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            cleanConnection(trackerServer, storageServer);
        }
    }

    public static String upload(byte[] data, String extName) {
        return upload(data, extName);
    }

    public static String uploadBase64(String base64string, String extName, String businessId) {
        NameValuePair metadata = new NameValuePair("businessId", businessId);
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes1 = decoder.decode(base64string);
        return upload(bytes1, extName, metadata);
    }

    public static String upload(File file, String path) {
        return upload(file, path);
    }

    public static String upload(String filePath) {
        return upload(new File(filePath), filePath);
    }

    /**
     * <B>方法名称：</B>下载方法<BR>
     * <B>概要说明：</B>通过文件id进行下载<BR>
     *
     * @param fileId 文件id
     * @return 返回InputStream
     */
    public static InputStream download(String groupName, String fileId) {
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        StorageClient1 storageClient1 = null;
        try {
            trackerServer = trackerClient.getConnection();
            if (trackerServer == null) {
                logger.info("getConnection return null");
            }
            storageServer = trackerClient.getStoreStorage(trackerServer, groupName);
            storageClient1 = new StorageClient1(trackerServer, storageServer);
            byte[] bytes = storageClient1.download_file1(fileId);
            InputStream inputStream = new ByteArrayInputStream(bytes);
            return inputStream;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            cleanConnection(trackerServer, storageServer);
        }
    }

    /**
     * <B>方法名称：</B>删除方法<BR>
     * <B>概要说明：</B>根据id来删除一个文件<BR>
     *
     * @param fileId 文件id
     * @return 删除成功返回0，非0则操作失败，返回错误代码
     */
    public static int delete(String groupName, String fileId) {
        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        StorageClient1 storageClient1;
        try {
            trackerServer = trackerClient.getConnection();
            if (trackerServer == null) {
                logger.info("getConnection return null");
            }
            storageServer = trackerClient.getStoreStorage(trackerServer, groupName);
            storageClient1 = new StorageClient1(trackerServer, storageServer);
            int result = storageClient1.delete_file1(fileId);
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        } finally {
            cleanConnection(trackerServer, storageServer);
        }
    }

    /**
     * <B>方法名称：</B><BR>
     * <B>概要说明：</B><BR>
     *
     * @param oldFileId 旧文件id
     * @param file      新文件
     * @param path      新文件路径
     * @return 上传成功返回id，失败返回null
     */
    public static String modify(String oldGroupName, String oldFileId, File file, String path) {
        return modify(oldGroupName, oldFileId, upload(file, path));
    }

    public static String modify(String oldGroupName, String oldFileId, byte[] data, String extName) {
        return modify(oldGroupName, oldFileId, upload(data, extName));
    }

    public static String modifyBase64(String oldGroupName, String oldFileId, String base64string, String extName, String businessId) {
        NameValuePair metadata = new NameValuePair("businessId", businessId);
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes1 = decoder.decode(base64string);
        return modify(oldGroupName, oldFileId, upload(bytes1, extName, metadata));
    }

    private static String modify(String oldGroupName, String oldFileId, String upload) {
        String fileid = null;
        try {
            // 先上传
            fileid = upload;
            if (fileid == null) {
                return null;
            }
            // 再删除
            int delResult = delete(oldGroupName, oldFileId);
            if (delResult != 0) {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return fileid;
    }

    /**
     * 获取远程服务器文件资源信息
     *
     * @param groupName 文件组名 如：group1
     * @param fileId    M00/00/00/wKgRsVjtwpSAXGwkAAAweEAzRjw471.jpg
     * @return
     */
    public static FileInfo getFile(String groupName, String fileId) {

        TrackerServer trackerServer = null;
        StorageServer storageServer = null;
        StorageClient1 storageClient1;
        try {
            trackerServer = trackerClient.getConnection();
            if (trackerServer == null) {
                logger.info("getConnection return null");
            }
            storageServer = trackerClient.getStoreStorage(trackerServer, groupName);
            storageClient1 = new StorageClient1(trackerServer, storageServer);
            return storageClient1.get_file_info1(fileId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            cleanConnection(trackerServer, storageServer);
        }
    }

    /**
     * <B>方法名称：</B>获取文件后缀名<BR>
     * <B>概要说明：</B>获取文件后缀名<BR>
     *
     * @param fileName
     * @return 如："jpg"、"txt"、"zip" 等
     */
    private static String getFileExt(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        } else {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
    }
}
