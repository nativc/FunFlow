package com.nativc.funflow.controller;

import com.nativc.funflow.common.Result;
import com.nativc.funflow.dto.request.UpdateProfileRequest;
import com.nativc.funflow.dto.response.AvatarUploadResponse;
import com.nativc.funflow.dto.response.UserProfileResponse;
import com.nativc.funflow.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户控制器
 * 用户信息相关接口
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前登录用户的个人资料
     *
     * @return 用户个人资料
     */
    @GetMapping("/profile")
    public Result<UserProfileResponse> getProfile() {
        UserProfileResponse profile = userService.getProfile();
        log.info("获取用户信息成功：{}", profile);
        return Result.success(profile);
    }

    /**
     * 上传用户头像
     *
     * @param file 头像文件
     * @return 头像 URL
     */
    @PostMapping("/profile/avatar")
    public Result<AvatarUploadResponse> uploadAvatar(@RequestParam("file") MultipartFile file) {
        String avatarUrl = userService.uploadAvatar(file);
        log.info("头像上传成功：{}", avatarUrl);
        return Result.success(new AvatarUploadResponse(avatarUrl));
    }

    /**
     * 更新当前登录用户的个人资料
     *
     * @param request 更新请求
     * @return 更新后的用户资料
     */
    @PostMapping("/profile")
    public Result<UserProfileResponse> updateProfile(@Valid @RequestBody UpdateProfileRequest request) {
        UserProfileResponse profile = userService.updateProfile(request);
        return Result.success(profile);
    }
}
