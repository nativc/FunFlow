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
