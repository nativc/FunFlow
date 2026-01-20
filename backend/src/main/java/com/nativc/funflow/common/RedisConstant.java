package com.nativc.funflow.common;

/**
 * Redis 常量类
 */
public class RedisConstant {

    /**
     * 图形验证码 Key 前缀
     */
    public static final String CAPTCHA_KEY_PREFIX = "captcha:";

    /**
     * 图形验证码过期时间（秒）
     */
    public static final long CAPTCHA_EXPIRE_SECONDS = 300; // 5分钟

    /**
     * 邮箱验证码 Key 前缀
     */
    public static final String EMAIL_CODE_KEY_PREFIX = "email_code:";

    /**
     * 邮箱验证码过期时间（秒）
     */
    public static final long EMAIL_CODE_EXPIRE_SECONDS = 300; // 5分钟

    /**
     * 用户注册锁 Key 前缀
     */
    public static final String REGISTER_LOCK_KEY_PREFIX = "register_lock:";

    /**
     * 用户注册锁过期时间（秒）
     */
    public static final long REGISTER_LOCK_EXPIRE_SECONDS = 10; // 10秒

    /**
     * 生成图形验证码的 Redis Key
     *
     * @param captchaId 验证码ID
     * @return Redis Key
     */
    public static String getCaptchaKey(String captchaId) {
        return CAPTCHA_KEY_PREFIX + captchaId;
    }

    /**
     * 生成邮箱验证码的 Redis Key
     *
     * @param email 邮箱地址
     * @return Redis Key
     */
    public static String getEmailCodeKey(String email) {
        return EMAIL_CODE_KEY_PREFIX + email.toLowerCase();
    }

    /**
     * 生成用户注册锁的 Redis Key
     *
     * @param email 邮箱地址
     * @return Redis Key
     */
    public static String getRegisterLockKey(String email) {
        return REGISTER_LOCK_KEY_PREFIX + email.toLowerCase();
    }
}
