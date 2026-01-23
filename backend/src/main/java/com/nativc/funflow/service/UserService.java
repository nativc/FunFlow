package com.nativc.funflow.service;

import com.nativc.funflow.dto.response.UserProfileResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 获取当前登录用户的个人资料
     *
     * @return 用户个人资料
     */
    UserProfileResponse getProfile();

    /**
     * 上传用户头像
     *
     * @param file 头像文件
     * @return 头像 URL
     */
    String uploadAvatar(MultipartFile file);
}
