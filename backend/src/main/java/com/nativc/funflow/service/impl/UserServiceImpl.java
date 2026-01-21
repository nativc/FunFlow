package com.nativc.funflow.service.impl;

import com.nativc.funflow.common.Code;
import com.nativc.funflow.common.UserContext;
import com.nativc.funflow.dto.response.UserProfileResponse;
import com.nativc.funflow.entity.User;
import com.nativc.funflow.exception.BusinessException;
import com.nativc.funflow.mapper.UserMapper;
import com.nativc.funflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

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
}
