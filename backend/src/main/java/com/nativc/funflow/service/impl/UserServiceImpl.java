package com.nativc.funflow.service.impl;

import com.nativc.funflow.common.Code;
import com.nativc.funflow.common.UserContext;
import com.nativc.funflow.config.OssConfig;
import com.nativc.funflow.dto.request.UpdateProfileRequest;
import com.nativc.funflow.dto.response.UserProfileResponse;
import com.nativc.funflow.entity.User;
import com.nativc.funflow.exception.BusinessException;
import com.nativc.funflow.mapper.UserMapper;
import com.nativc.funflow.service.OssService;
import com.nativc.funflow.service.UserService;
import com.nativc.funflow.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

/**
 * 用户服务实现类
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OssService ossService;

    @Autowired
    private OssConfig ossConfig;

    /**
     * 头像允许的图片格式
     */
    private static final List<String> AVATAR_ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");

    /**
     * 头像最大文件大小：5MB
     */
    private static final long AVATAR_MAX_SIZE = 5 * 1024 * 1024;

    private static final String AVATAR_BASE_PATH = "user/avatar/";

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
        String fileName = FileUtil.generateUniqueFileName(AVATAR_BASE_PATH, userId.toString(), extension);

        // 4. 上传到 OSS
        return ossService.uploadFile(file, fileName);
    }

    @Override
    public UserProfileResponse updateProfile(UpdateProfileRequest request) {
        // 1. 获取当前用户 ID
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException(Code.UNAUTHORIZED, "未登录");
        }

        // 2. 检查是否至少有一个字段需要更新
        if (request.getUsername() == null && request.getNickname() == null &&
                request.getAvatarUrl() == null && request.getBio() == null) {
            throw new BusinessException(Code.ERROR, "至少需要更新一个字段");
        }

        // 3. 如果要更新 username，检查是否被占用
        if (request.getUsername() != null) {
            int count = userMapper.countByUsernameExcludingSelf(request.getUsername(), userId);
            if (count > 0) {
                throw new BusinessException(Code.BUSINESS_ERROR, "该账号已被使用");
            }
        }

        // 4. 如果要更新 avatarUrl，校验 URL 格式和路径
        if (request.getAvatarUrl() != null && !request.getAvatarUrl().isEmpty()) {
            // 校验路径格式：{basePath}/user/avatar/{userId}/xxx
            String stdUrlPrefix = ossConfig.getOssUrlPrefix() + AVATAR_BASE_PATH + userId;
            if (!request.getAvatarUrl().startsWith(stdUrlPrefix)) {
                throw new BusinessException(Code.ERROR, "头像 URL 格式不正确");
            }
        }

        // 5. 构建更新对象
        User user = User.builder()
                .userId(userId)
                .username(request.getUsername())
                .nickname(request.getNickname())
                .avatarUrl(request.getAvatarUrl())
                .bio(request.getBio())
                .build();

        // TODO: 头像、签名审核

        // 6. 更新数据库
        int rows = userMapper.updateProfile(user);
        if (rows == 0) {
            throw new BusinessException(Code.ERROR, "更新失败");
        }

        log.info("更新个人资料成功：{}", user);
        return getProfile();
    }
}
