package com.nativc.funflow.util;

import com.nativc.funflow.common.Code;
import com.nativc.funflow.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * 文件工具类
 */
public class FileUtil {

    /**
     * 校验文件是否为空
     */
    public static void validateFileNotEmpty(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(Code.ERROR, "文件不能为空");
        }
    }

    /**
     * 校验文件大小
     */
    public static void validateFileSize(MultipartFile file, long maxSize, String maxSizeText) {
        if (file.getSize() > maxSize) {
            throw new BusinessException(Code.ERROR, "文件大小不能超过 " + maxSizeText);
        }
    }

    /**
     * 校验文件扩展名
     */
    public static String validateAndGetExtension(MultipartFile file, List<String> allowedExtensions) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new BusinessException(Code.ERROR, "文件名不能为空");
        }

        String extension = getFileExtension(originalFilename);
        if (!allowedExtensions.contains(extension.toLowerCase())) {
            throw new BusinessException(Code.ERROR, 
                    "仅支持 " + String.join("、", allowedExtensions) + " 格式的文件");
        }

        return extension;
    }

    /**
     * 获取文件扩展名
     */
    public static String getFileExtension(String filename) {
        int lastIndexOf = filename.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return filename.substring(lastIndexOf + 1);
    }

    /**
     * 生成唯一文件名
     *
     * @param basePath  基础路径，如 "user/avatar/"
     * @param subPath   子路径，如 userId
     * @param extension 文件扩展名
     * @return 完整的文件路径，如 "user/avatar/10001/uuid.jpg"
     */
    public static String generateUniqueFileName(String basePath, String subPath, String extension) {
        return basePath + subPath + "/" + UUID.randomUUID() + "." + extension;
    }
}
