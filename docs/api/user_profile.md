# 用户信息相关接口

## 获取用户信息

获取当前登录用户的个人资料信息，用于展示个人主页。

**接口地址：** `GET /api/user/profile`

**请求头：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| Authorization | string | 是 | Bearer Token，格式：`Bearer {accessToken}` |

**响应示例：**
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "userId": 10001,
    "username": "john_doe",
    "nickname": "John",
    "avatarUrl": "https://cdn.example.com/avatars/10001.jpg",
    "bio": "热爱生活，喜欢分享",
    "followingCount": 128,
    "followerCount": 1024,
    "likesReceived": 8888
  }
}
```

**响应参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| userId | number | 用户唯一 ID |
| username | string | 账号（唯一标识） |
| nickname | string | 昵称 |
| avatarUrl | string | 头像 URL，可能为空 |
| bio | string | 个性签名/个人简介 |
| followingCount | number | 关注数 |
| followerCount | number | 粉丝数 |
| likesReceived | number | 作品总获赞量 |

## 用户头像上传

上传用户头像图片文件，后端将图片上传到 OSS 并返回图片 URL。

**接口地址：** `POST /api/user/profile/avatar`

**请求头：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| Authorization | string | 是 | Bearer Token，格式：`Bearer {accessToken}` |
| Content-Type | string | 是 | multipart/form-data |

**请求参数（Form Data）：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| file | File | 是 | 头像图片文件，支持 jpg/png/jpeg，建议尺寸 200x200，最大 5MB |

**响应示例：**
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "avatarUrl": "https://your-bucket.oss-cn-hangzhou.aliyuncs.com/user/avatar/12345/1a2b3c.jpg"
  }
}
```

**响应参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| avatarUrl | string | 上传成功后的头像 URL，需保存此 URL 用于后续资料更新 |


## 用户资料更新

更新当前登录用户的个人资料，包括昵称、账号、头像 URL 和个性签名。

**接口地址：** `POST /api/user/profile`

**请求头：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| Authorization | string | 是 | Bearer Token，格式：`Bearer {accessToken}` |
| Content-Type | string | 是 | application/json |

**请求参数（JSON Body）：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | string | 否 | 账号（唯一标识），3-20 个字符，只能包含字母、数字、下划线，不区分大小写 |
| nickname | string | 否 | 昵称，1-20 个字符 |
| avatarUrl | string | 否 | 头像 URL，需先通过头像上传接口获取 |
| bio | string | 否 | 个性签名/个人简介，最多 100 个字符 |

**请求示例：**
```json
{
  "username": "john_doe_2025",
  "nickname": "John Doe",
  "avatarUrl": "https://your-bucket.oss-cn-hangzhou.aliyuncs.com/user/avatar/12345/1a2b3c.jpg",
  "bio": "热爱生活，喜欢分享，记录美好瞬间"
}
```

**注意事项：**
- 所有字段均为可选，只传需要更新的字段即可
- 修改账号时会进行唯一性校验（忽略大小写）
- 头像和个性签名可能需要经过内容审核，审核通过后才生效（当前版本暂不支持审核，直接更新）
