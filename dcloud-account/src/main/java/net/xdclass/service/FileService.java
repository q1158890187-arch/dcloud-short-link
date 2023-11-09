package net.xdclass.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @description:
 * @author: zhengqinghua
 * @date: 2023/11/9 23:57
 */
public interface FileService {

    /**
     * 文件上传
     * @param file
     * @return
     */
    String uploadUserImg(MultipartFile file);
}