package com.nativc.funflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nativc.funflow.dto.request.SendEmailCodeRequest;
import com.nativc.funflow.service.AuthService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * 异常类单元测试
 * 测试业务异常和系统异常的各种场景
 */
@SpringBootTest
@AutoConfigureMockMvc
class ExceptionTests {

    @Autowired
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testMethodArgumentNotValidException() throws Exception {
        SendEmailCodeRequest request = new SendEmailCodeRequest();
        request.setEmail(""); // 空邮箱，违反 @NotBlank 约束
        request.setCaptchaId("test-id");
        request.setCaptchaText("1234");
        
        mockMvc.perform(post("/api/auth/send-email-code")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void testConstraintViolationException() {
        SendEmailCodeRequest request = new SendEmailCodeRequest();
        request.setEmail("adminqq.com");
        assertThrows(ConstraintViolationException.class, () -> authService.sendEmailCode(request));
    }
}
