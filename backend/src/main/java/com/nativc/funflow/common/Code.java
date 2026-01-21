package com.nativc.funflow.common;

/**
 * 返回状态码定义类
 */
public class Code {

    /**
     * 默认请求成功状态码
     */
    public static final int SUCCESS = 200;
    /**
     * 默认请求失败状态吗
     */
    public static final int ERROR = 400;
    /**
     * 业务逻辑异常
     */
    public static final int BUSINESS_ERROR = 40001;
    /**
     * 系统异常
     */
    public static final int SYSTEM_ERROR = 40002;

    /**
     * 请求参数格式错误
     */
    public static final int VALIDATION_ERROR = 40003;

    /**
     * 服务不可用
     */
    public static final int SERVICE_UNAVAILABLE = 40005;

    /**
     * 未授权（未登录或登录过期）
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 资源不存在
     */
    public static final int NOT_FOUND = 404;
}
