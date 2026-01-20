package com.nativc.funflow.common;

/**
 * 用户上下文
 * 使用 ThreadLocal 存储当前请求的用户ID
 */
public class UserContext {

    private static final ThreadLocal<Long> USER_ID = new ThreadLocal<>();

    /**
     * 设置当前用户ID
     *
     * @param userId 用户ID
     */
    public static void setUserId(Long userId) {
        USER_ID.set(userId);
    }

    /**
     * 获取当前用户ID
     *
     * @return 用户ID，未登录时返回 null
     */
    public static Long getUserId() {
        return USER_ID.get();
    }

    /**
     * 清除当前用户信息
     * 在请求结束时调用，防止内存泄漏
     */
    public static void clear() {
        USER_ID.remove();
    }
}
