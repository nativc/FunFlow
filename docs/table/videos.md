# 短视频表设计

## 需求分析

- 用户可以上传自己的视频进行作品创作，需提供的信息包括：视频、封面（可以为空）、文案、标签、是否公开
- 在浏览视频时，可以看到的信息包括：视频、封面、作者、标题（文案）、标签、点赞数、收藏数、评论数
- 在进行视频管理时，需记录视频创建时间、状态（审核中、已发布、已下架、违规）
- 视频发布后，视频、封面、标签无法进行修改
- 草稿状态由前端本地实现，无需存储到数据库

## 表字段设计

### videos 表

**基本信息字段**
- `id` (BIGINT)，视频唯一标识，主键
- `user_id` (BIGINT)，上传用户的 ID，外键关联 users 表
- `video_url` (VARCHAR(512))，视频文件 URL
- `cover_url` (VARCHAR(512))，视频封面图片 URL，可为空
- `title` (VARCHAR(300))，视频标题（文案）

**互动数据字段**（冗余字段，提升查询性能）
- `view_count` (INT)，播放量，默认 0
- `like_count` (INT)，点赞量，默认 0
- `collect_count` (INT)，收藏数，默认 0
- `comment_count` (INT)，评论数，默认 0

**状态控制字段**
- `is_public` (TINYINT)，是否公开，默认 1
  - 0: 私密
  - 1: 公开
- `status` (TINYINT)，审核状态，默认 0
  - 0: 审核中
  - 1: 已发布
  - 2: 已下架
  - 3: 违规

**审核相关字段**
- `audit_time` (TIMESTAMP)，审核时间，可为空
- `audit_reason` (VARCHAR(500))，审核备注/拒绝原因，可为空

**时间戳**
- `created_at` (TIMESTAMP)，创建时间，默认当前时间
- `updated_at` (TIMESTAMP)，更新时间，自动更新
- `deleted_at` (TIMESTAMP)，软删除时间，可为空

**索引设计**
- PRIMARY KEY (`id`)
- INDEX `idx_user_status` (`user_id`, `status`)

**SQL 创建语句**
```sql
CREATE TABLE `videos` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '视频唯一标识',
  `user_id` BIGINT NOT NULL COMMENT '上传用户的ID',
  `video_url` VARCHAR(512) NOT NULL COMMENT '视频文件URL',
  `cover_url` VARCHAR(512) DEFAULT NULL COMMENT '视频封面图片URL',
  `title` VARCHAR(300) NOT NULL COMMENT '视频标题（文案）',
  `view_count` INT NOT NULL DEFAULT 0 COMMENT '播放量',
  `like_count` INT NOT NULL DEFAULT 0 COMMENT '点赞量',
  `collect_count` INT NOT NULL DEFAULT 0 COMMENT '收藏数',
  `comment_count` INT NOT NULL DEFAULT 0 COMMENT '评论数',
  `is_public` TINYINT NOT NULL DEFAULT 1 COMMENT '是否公开：0-私密，1-公开',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '审核状态：0-审核中，1-已发布，2-已下架，3-违规',
  `audit_time` TIMESTAMP NULL DEFAULT NULL COMMENT '审核时间',
  `audit_reason` VARCHAR(500) DEFAULT NULL COMMENT '审核备注/拒绝原因',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted_at` TIMESTAMP NULL DEFAULT NULL COMMENT '软删除时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_status` (`user_id`, `status`),
  INDEX `idx_created_at` (`created_at`),
  INDEX `idx_deleted_at` (`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='短视频表';
```

### video_tags 表（视频标签关联表）

用于实现视频与标签的多对多关系，支持高效的标签查询和统计。

**字段设计**
- `id` (BIGINT)，关联记录唯一标识，主键
- `video_id` (BIGINT)，视频 ID，外键关联 videos 表
- `tag_id` (BIGINT)，标签 ID，外键关联 tags 表
- `created_at` (TIMESTAMP)，创建时间，默认当前时间

**索引设计**
- PRIMARY KEY (`id`)
- UNIQUE INDEX `uk_video_tag` (`video_id`, `tag_id`) -- 防止重复关联
- INDEX `idx_tag_id` (`tag_id`) -- 用于按标签查询视频

**说明**
- 视频发布后，关联的标签不可修改
- 删除视频时，需同步删除 video_tags 中的关联记录

**SQL 创建语句**
```sql
CREATE TABLE `video_tags` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联记录唯一标识',
  `video_id` BIGINT NOT NULL COMMENT '视频ID',
  `tag_id` BIGINT NOT NULL COMMENT '标签ID',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_video_tag` (`video_id`, `tag_id`),
  INDEX `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='视频标签关联表';
```

### tags 表（标签字典表）

管理系统中所有的标签信息。

**字段设计**
- `id` (BIGINT)，标签唯一标识，主键
- `tag_name` (VARCHAR(50))，标签名称，唯一
- `use_count` (INT)，使用次数，默认 0（冗余字段，用于热门标签统计）
- `created_at` (TIMESTAMP)，创建时间，默认当前时间

**索引设计**
- PRIMARY KEY (`id`)
- UNIQUE INDEX `uk_tag_name` (`tag_name`)
- INDEX `idx_use_count` (`use_count`) -- 用于热门标签排序

**SQL 创建语句**
```sql
CREATE TABLE `tags` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '标签唯一标识',
  `tag_name` VARCHAR(50) NOT NULL COMMENT '标签名称',
  `use_count` INT NOT NULL DEFAULT 0 COMMENT '使用次数（冗余字段，用于热门标签统计）',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `uk_tag_name` (`tag_name`),
  INDEX `idx_use_count` (`use_count`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签字典表';
```

## 业务规则

1. **视频发布流程**：
   - 用户上传视频后，状态为"审核中"（status=0）
   - 审核通过后，状态更新为"已发布"（status=1）
   - 审核不通过则标记为"违规"（status=3）

2. **标签管理**：
   - 标签由系统预设或用户首次使用时自动创建
   - 每个视频可关联多个标签（建议限制 3-5 个）
   - 视频发布后，标签关联不可修改

3. **软删除机制**：
   - 用户删除视频时，设置 `deleted_at` 字段
   - 查询时需过滤 `deleted_at IS NULL` 的记录

4. **互动数据更新**：
   - 点赞、收藏、评论等操作通过消息队列异步更新计数字段
   - 定期校验计数准确性

