package com.nativc.funflow.common;

import lombok.Data;

/**
 * 统一返回结果封装类
 *
 * @param <T> 返回数据类型
 */
@Data
public class Result<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;


    public Result() {
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回（无数据）
     */
    public static <T> Result<T> success() {
        return new Result<>(Code.SUCCESS, "success", null);
    }

    /**
     * 成功返回（带数据）
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(Code.SUCCESS, "success", data);
    }

    /**
     * 失败返回（无数据）
     */
    public static <T> Result<T> error() {
        return new Result<>(Code.ERROR, "error", null);
    }

    /**
     * 失败返回（带错误码、消息）
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }

}