package com.nativc.funflow.controller;

import com.nativc.funflow.common.Result;
import com.nativc.funflow.dto.response.UserProfileResponse;
import com.nativc.funflow.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 * 用户信息相关接口
 */
@RestController
@RequestMapping("/user")
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
        return Result.success(profile);
    }
}
