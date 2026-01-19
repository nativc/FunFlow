package com.nativc.funflow.service;

import com.nativc.funflow.dto.response.CaptchaResponse;

public interface AuthService {

    /**
     * 生成图形验证码
     *
     * @return 验证码信息（包含 captchaId 和 imageData）
     */
    CaptchaResponse generateCaptcha();
}
