package com.nativc.funflow.controller;


import com.nativc.funflow.common.Result;
import com.nativc.funflow.dto.response.CaptchaResponse;
import com.nativc.funflow.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 认证控制器
 * 登录、注册相关接口
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 获取图形验证码
     *
     * @return 验证码信息
     */
    @GetMapping("/captcha")
    public Result<CaptchaResponse> getCaptcha() {
        CaptchaResponse captcha = authService.generateCaptcha();
        return Result.success(captcha);
    }
}
