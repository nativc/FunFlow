# 视频相关接口

## 创建视频作品

用户上传视频进行作品创作，包括视频文件、封面图片（可选）、文案、标签和公开属性设置。视频上传后将进入审核流程。

**接口地址：** `POST /api/videos`

**请求头：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| Authorization | string | 是 | Bearer Token，格式：`Bearer {accessToken}` |
| Content-Type | string | 是 | multipart/form-data |

**请求参数（Form Data）：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| video | File | 是 | 视频文件，支持 mp4/mov/avi 格式，建议分辨率 720p 及以上，最大 300MB |
| cover | File | 否 | 封面图片文件，支持 jpg/png/jpeg，建议尺寸 16:9，最大 5MB。若不传则使用视频第一帧作为封面 |
| title | string | 否 | 视频标题（文案），最多 300 个字符，默认为空 |
| tags | string | 是 | 标签列表，JSON 数组格式，最少 1 个，最多 5 个。示例：`["美食","旅行","生活"]` |
| isPublic | number | 否 | 是否公开，0-私密，1-公开，默认 1 |

**请求示例：**
```
POST /api/videos
Content-Type: multipart/form-data

video: <video_file>
cover: <cover_image_file>
title: "周末探店｜这家咖啡店的拿铁真的绝了！"
tags: ["咖啡","探店","生活记录"]
isPublic: 1
```

## 获取用户作品列表

获取当前登录用户的作品列表，用于在个人主页的作品标签下展示。

**接口地址：** `GET /api/videos`

**请求头：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| Authorization | string | 是 | Bearer Token，格式：`Bearer {accessToken}` |

**请求参数（Query）：**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | number | 否 | 页码，从 1 开始，默认 1 |
| pageSize | number | 否 | 每页数量，默认 20，最大 50 |

**请求示例：**
```
GET /api/videos?page=1&pageSize=20
```

**响应示例：**
```json
{
  "code": 200,
  "msg": "success",
  "data": {
    "userId": 10001,
    "total": 42,
    "page": 1,
    "pageSize": 20,
    "videos": [
      {
        "videoId": 20001,
        "title": "周末探店｜这家咖啡店的拿铁真的绝了！",
        "coverUrl": "https://cdn.example.com/covers/10001/cover.jpg",
        "videoUrl": "https://cdn.example.com/videos/10001/video.mp4",
        "tags": ["咖啡", "探店", "生活记录"],
        "viewCount": 1234,
        "likeCount": 88,
        "status": 1,
        "isPublic": 1,
        "createTime": "2026-01-26 14:30:00"
      },
      {
        "videoId": 20002,
        "title": "分享一个超好吃的家常菜",
        "coverUrl": null,
        "videoUrl": "https://cdn.example.com/videos/10001/video.mp4",
        "tags": ["美食", "家常菜"],
        "viewCount": 567,
        "likeCount": 45,
        "status": 0,
        "isPublic": 1,
        "createTime": "2026-01-25 10:20:00"
      }
    ]
  }
}
```

**响应参数：**

| 参数 | 类型 | 说明 |
|------|------|------|
| userId | number | 用户 ID，标识当前作品列表所属用户 |
| total | number | 作品总数（不包含违规视频） |
| page | number | 当前页码 |
| pageSize | number | 每页数量 |
| videos | array | 作品列表，按创建时间倒序排序 |

**videos 数组元素说明：**

| 参数 | 类型 | 说明 |
|------|------|------|
| videoId | number | 视频唯一 ID |
| title | string | 视频标题，可能为空字符串 |
| coverUrl | string\|null | 封面图片 URL，若为 null 则前端使用视频第一帧作为封面 |
| videoUrl | string | 视频文件 URL |
| tags | array | 标签列表，字符串数组 |
| viewCount | number | 观看量 |
| likeCount | number | 点赞量 |
| status | number | 视频状态：0-审核中、1-已发布、2-已下架、3-违规 |
| isPublic | number | 是否公开，0-私密，1-公开 |
| createTime | string | 创建时间，格式：yyyy-MM-dd HH:mm:ss |

**注意事项：**
- 违规视频（status=3）不会出现在作品列表中
- 封面 URL 为 null 时，前端使用 videoUrl 提取视频第一帧作为封面
- 作品按 createTime（创建时间）倒序排序
- 作品描述由前端拼接：格式为「标题#标签1#标签2#...」，若标题为空则为「#标签1#标签2#...」
- 右上角作品状态的显示逻辑：
  - status=0：显示"审核中"
  - status=1 且 isPublic=1：显示"公开"
  - status=1 且 isPublic=0：显示"私密"
  - status=2：显示"已下架"
