package com.nativc.funflow.service.impl;

import com.nativc.funflow.common.Code;
import com.nativc.funflow.common.UserContext;
import com.nativc.funflow.dto.response.UserProfileResponse;
import com.nativc.funflow.entity.User;
import com.nativc.funflow.exception.BusinessException;
import com.nativc.funflow.mapper.UserMapper;
import com.nativc.funflow.service.OssService;
import com.nativc.funflow.service.UserService;
import com.nativc.funflow.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OssService ossService;

    /**
     * 头像允许的图片格式
     */
    private static final List<String> AVATAR_ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");

    /**
     * 头像最大文件大小：5MB
     */
    private static final long AVATAR_MAX_SIZE = 5 * 1024 * 1024;

    @Override
    public UserProfileResponse getProfile() {
        // 1. 从 ThreadLocal 获取当前用户 ID
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(Code.UNAUTHORIZED, "未登录");
        }

        // 2. 查询用户信息
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException(Code.NOT_FOUND, "用户不存在");
        }

        // 3. 构建响应对象
        return UserProfileResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .bio(user.getBio())
                .followingCount(user.getFollowingCount())
                .followerCount(user.getFollowerCount())
                .likesReceived(user.getCachedTotalLikesReceived())
                .build();
    }

    @Override
    public String uploadAvatar(MultipartFile file) {
        // 1. 获取当前用户 ID
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(Code.UNAUTHORIZED, "未登录");
        }

        // 2. 校验文件
        FileUtil.validateFileNotEmpty(file);
        FileUtil.validateFileSize(file, AVATAR_MAX_SIZE, "5MB");
        String extension = FileUtil.validateAndGetExtension(file, AVATAR_ALLOWED_EXTENSIONS);

        // 3. 生成文件路径：user/avatar/{userId}/{uuid}.{extension}
        String fileName = FileUtil.generateUniqueFileName( "user/avatar/", userId.toString(), extension);

        // 4. 上传到 OSS
        return ossService.uploadFile(file, fileName);
    }
}
