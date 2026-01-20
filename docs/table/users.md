# 用户表设计

## 需求分析

**包含字段：**
- 用户在进行账号注册时，需提供邮箱和密码；通过邮箱和密码进行登录
- 个人主页展示的信息包括：头像、昵称、账号、关注数、粉丝数、作品总获赞量、简介
- 账号状态信息：创建时间、最后登录时间、状态（正常、禁用等）

**字段说明：**
- 邮箱、账号：唯一性约束，邮箱在数据库中转小写存储，账号查询时忽略大小写
- 密码：存储密码哈希，保证安全性
- 关注数、粉丝数：作为冗余字段存储在用户表中，方便快速查询；通过事务保证数据一致性
- 获赞量：基于更新效率考量，用户表存储的是缓存值，通过定时任务进行字段更新
- 状态：`0` 表示正常，`1` 表示账号被封禁，`2` 表示账号被注销（还未被删除）

**索引：**
- 邮箱、账号字段存在唯一性约束，MySQL 会自动创建唯一索引
- 在 `status` 字段建立索引，方便快速查询账号状态

## SQL

```sql
CREATE TABLE `users` (
  `user_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户唯一ID',
  
  -- 账号凭证字段
  `email` VARCHAR(255) NOT NULL COMMENT '邮箱（转小写存储）',
  `password_hash` CHAR(60) NOT NULL COMMENT '密码哈希值（如 bcrypt）',
  `username` VARCHAR(64) NOT NULL COMMENT '账号（唯一性标识，查询时忽略大小写）',
  
  -- 个人主页展示字段
  `avatar_url` VARCHAR(512) DEFAULT NULL COMMENT '头像URL',
  `nickname` VARCHAR(64) NOT NULL COMMENT '昵称',
  `bio` TEXT DEFAULT NULL COMMENT '个人简介',
  
  -- 统计字段（用于快速展示）
  `following_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '关注数',
  `follower_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '粉丝数',
  `cached_total_likes_received` BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '作品总获赞量（缓存值，由定时任务更新）',
  
  -- 账号状态与时间信息
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '账号创建时间',
  `last_login_at` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `deleted_at` DATETIME DEFAULT NULL COMMENT '注销时间',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '账号状态：0-正常，1-封禁，2-注销',
  
  -- 主键
  PRIMARY KEY (`user_id`),
  
  -- 唯一性约束（自动创建唯一索引）
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_username` (`username`),
  
  -- 状态字段索引（便于按状态筛选用户）
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
```

## 设计决策

### 为什么获赞量是一个 cache，而非实时更新的字段？
直接在用户表中添加一个 `total_likes_received` 字段，属于“反范式化”设计。虽然它可能在查询时非常快，但会带来一系列严重的问题：
- 点赞/取消属于频繁的写入操作。如果获赞量单独存储，每当视频的点赞数发生变化，都需要同时更新两个表：视频表和用户表。
- 频繁同时更新两张表，会大大增加数据库的写入压力。高并发场景下，存在数据不一致风险。

**解决方案：异步计算 + 定期更新**
- 不把 `total_likes_received` 作为数据库中的一个持久化字段，而是作为一个可以延迟更新的“缓存值”。
- 通过定时任务（例如，每10分钟或每小时）运行一次聚合查询，更新这个缓存字段。
- 优点：查询性能高、减轻写入压力、最终一致性。

### 为什么关注数和粉丝数作为冗余字段实时更新？
关注/粉丝数的写操作（关注/取关）相对稀少，但读操作极其频繁。这种“写少读多”的模式是使用冗余计数字段的理想场景。

**数据一致性方案：** 在关注/取关操作时，在同一个数据库事务内更新关系表和计数字段。
