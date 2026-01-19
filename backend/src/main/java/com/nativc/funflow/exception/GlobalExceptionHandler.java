package com.nativc.funflow.exception;

import com.nativc.funflow.common.Code;
import com.nativc.funflow.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 统一处理系统中的各类异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * dev 开发，test 测试，prod 生产
     */
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;
    
    /**
     * 处理业务异常
     * 自定义的业务逻辑异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常 [URI: {}]: {}", request.getRequestURI(), e.getMessage());
        return Result.error(Code.BUSINESS_ERROR, e.getMessage());
    }
    
    /**
     * 处理所有未捕获的异常
     * 兜底异常处理
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常 [URI: {}]: ", request.getRequestURI(), e);
        
        // 生产环境隐藏详细错误信息
        String message = isProduction() ? "系统繁忙，请稍后重试" : e.getMessage();
        return Result.error(Code.SYSTEM_ERROR, message);
    }
    
    /**
     * 判断是否为生产环境
     */
    private boolean isProduction() {
        return "prod".equalsIgnoreCase(activeProfile);
    }
}
