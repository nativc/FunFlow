package com.nativc.funflow;

import com.nativc.funflow.common.Code;
import com.nativc.funflow.common.Result;
import com.nativc.funflow.exception.BusinessException;
import com.nativc.funflow.exception.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * 异常类单元测试
 * 测试业务异常和系统异常的各种场景
 */
@ExtendWith(MockitoExtension.class)
class ExceptionTests {

    @Mock
    private HttpServletRequest request;

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleBusinessException() {
        // Given
        BusinessException exception = new BusinessException(40001, "用户不存在");
        when(request.getRequestURI()).thenReturn("/api/user/123");

        // When
        Result<Void> result = exceptionHandler.handleBusinessException(exception, request);

        // Then
        assertEquals(Code.BUSINESS_ERROR, result.getCode());
        assertEquals("用户不存在", result.getMessage());
        assertNull(result.getData());
    }

    @Test
    void testHandleSystemExceptionInDevEnvironment() {
        // Given
        ReflectionTestUtils.setField(exceptionHandler, "activeProfile", "dev");
        Exception exception = new NullPointerException("空指针异常");
        when(request.getRequestURI()).thenReturn("/api/test");

        // When
        Result<Void> result = exceptionHandler.handleException(exception, request);

        // Then
        assertEquals(Code.SYSTEM_ERROR, result.getCode());
        assertEquals("空指针异常", result.getMessage());
    }
}
