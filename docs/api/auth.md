# 登录注册相关接口

## 获取图形验证码

获取登录/注册所需的图形验证码。

**接口地址：** `GET /api/auth/captcha`

**响应示例：**
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "captchaId": "a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8",
    "imageData": "data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL..."
  }
}
```

**响应参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| captchaId | string | 验证码唯一标识，用于后续提交时校验 |
| imageData | string | base64 编码的图片数据 |

---

## 发送邮箱验证码

向指定邮箱发送 6 位数字验证码，用于注册验证。

**接口地址：** `POST /api/auth/send-email-code`

**请求示例：**
```json
{
  "email": "user@example.com",
  "captchaId": "a1b2c3d4-e5f6-7890-g1h2-i3j4k5l6m7n8",
  "captchaText": "A3b7"
}
```

**请求参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| email | string | 是 | 用户邮箱地址 |
| captchaId | string | 是 | 图形验证码 ID |
| captchaText | string | 是 | 图形验证码文本 |

---

## 用户注册

使用邮箱和邮箱验证码完成用户注册。

**接口地址：** `POST /api/auth/register`

**请求示例：**
```json
{
  "email": "user@example.com",
  "emailCode": "654321",
  "password": "UserSetPlainTextPassword123!"
}
```

**请求参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| email | string | 是 | 用户邮箱地址 |
| emailCode | string | 是 | 邮箱收到的 6 位验证码 |
| password | string | 是 | 用户设置的密码 |

---

## 用户登录

使用邮箱和密码进行登录，获取访问令牌。

**接口地址：** `POST /api/auth/login`

**请求示例：**
```json
{
  "email": "user@example.com",
  "password": "UserSetPlainTextPassword123!",
  "captchaId": "login_captcha_123456",
  "captchaText": "X8y9"
}
```

**请求参数：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| email | string | 是 | 用户邮箱地址 |
| password | string | 是 | 用户密码 |
| captchaId | string | 是 | 图形验证码 ID |
| captchaText | string | 是 | 图形验证码文本 |

**响应示例：**
```json
{
  "code": 200,
  "msg": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

**响应参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| accessToken | string | JWT 访问令牌，用于后续接口认证 |

> 后期引入 refreshToken 进行令牌的刷新
