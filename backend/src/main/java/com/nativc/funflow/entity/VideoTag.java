package com.nativc.funflow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 视频标签关联实体类
 * 对应数据库表: video_tags
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoTag {

    /**
     * 关联记录唯一标识
     */
    private Long videoTagId;

    /**
     * 视频ID
     */
    private Long videoId;

    /**
     * 标签ID
     */
    private Long tagId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
