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
