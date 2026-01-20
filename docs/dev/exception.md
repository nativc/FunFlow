## 自定义业务异常

基础业务异常：
```java
public class BaseException extends RuntimeException {
    private String code;
    private String message;
    
    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
```

具体业务异常：
```java
public class BusinessException extends BaseException {
    public BusinessException(String message) {
        super("BUSINESS_ERROR", message);
    }
    
    public BusinessException(String code, String message) {
        super(code, message);
    }
}
```

## 参数校验异常

定义方法参数校验规则：

```java
public class SendEmailCodeRequest {
    
    /**
     * 邮箱地址
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
}
```

Controller 中使用 `@Valid` + `@RequestBody` 注解校验参数：
```java
@PostMapping("/send-email-code")
    public Result<Void> sendEmailCode(@Valid @RequestBody SendEmailCodeRequest request) {
        authService.sendEmailCode(request);
        return Result.success();
    }
```
校验失败抛出 `MethodArgumentNotValidException`.

Service 中使用 `@Validated` 注解：
```java
@Validated
public class AuthServiceImpl implements AuthService {
    public void sendEmailCode(SendEmailCodeRequest request) {

    }
}
```

校验失败抛出 `ConstraintViolationException`.

## 全局异常处理

使用 `@ControllerAdvice` 来统一处理异常，返回自定义的错误格式。
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理业务异常
     * 自定义的业务逻辑异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e, HttpServletRequest request) {
        return Result.error(Code.BUSINESS_ERROR, e.getMessage());
    }

    /**
     * 处理 @RequestBody + @Valid 参数校验异常
     * 主要用于 Controller 方法参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e,
                                                  HttpServletRequest request) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return Result.error(Code.VALIDATION_ERROR, message);
    }

    /**
     * 处理 Service 方法参数校验异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(
            ConstraintViolationException e, HttpServletRequest request) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        return Result.error(Code.VALIDATION_ERROR, message);
    }
    
    /**
     * 处理所有未捕获的异常
     * 兜底异常处理
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e, HttpServletRequest request) {        
        // 生产环境隐藏详细错误信息
        String message = isProduction() ? "系统繁忙，请稍后重试" : e.getMessage();
        return Result.error(Code.SYSTEM_ERROR, message);
    }
}
```
