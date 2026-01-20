package com.nativc.funflow.service;

import com.nativc.funflow.dto.request.LoginRequest;
import com.nativc.funflow.dto.request.RegisterRequest;
import com.nativc.funflow.dto.request.SendEmailCodeRequest;
import com.nativc.funflow.dto.response.CaptchaResponse;
import com.nativc.funflow.dto.response.LoginResponse;
import jakarta.validation.Valid;

public interface AuthService {

    /**
     * 生成图形验证码
     *
     * @return 验证码信息（包含 captchaId 和 imageData）
     */
    CaptchaResponse generateCaptcha();

    /**
     * 发送邮箱验证码
     *
     * @param request 发送邮箱验证码请求
     */
    void sendEmailCode(@Valid SendEmailCodeRequest request);

    /**
     * 用户注册
     *
     * @param request 注册请求
     */
    void register(@Valid RegisterRequest request);

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录响应（包含 accessToken）
     */
    LoginResponse login(@Valid LoginRequest request);
}
