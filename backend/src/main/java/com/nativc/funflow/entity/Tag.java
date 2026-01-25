package com.nativc.funflow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 标签实体类
 * 对应数据库表: tags
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    /**
     * 标签唯一标识
     */
    private Long tagId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 使用次数（冗余字段，用于热门标签统计）
     */
    private Integer useCount;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
