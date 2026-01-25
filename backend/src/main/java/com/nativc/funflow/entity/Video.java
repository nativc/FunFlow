package com.nativc.funflow.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 视频实体类
 * 对应数据库表: videos
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Video {

    /**
     * 视频唯一标识
     */
    private Long videoId;

    /**
     * 上传用户的ID
     */
    private Long userId;

    /**
     * 视频文件URL
     */
    private String videoUrl;

    /**
     * 视频封面图片URL
     */
    private String coverUrl;

    /**
     * 视频标题（文案）
     */
    private String title;

    /**
     * 播放量
     */
    private Integer viewCount;

    /**
     * 点赞量
     */
    private Integer likeCount;

    /**
     * 收藏数
     */
    private Integer collectCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 是否公开：0-私密，1-公开
     */
    private Integer isPublic;

    /**
     * 审核状态：0-审核中，1-已发布，2-已下架，3-违规
     */
    private Integer status;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核备注/拒绝原因
     */
    private String auditReason;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 软删除时间
     */
    private LocalDateTime deletedAt;

    /**
     * 审核状态枚举
     */
    public enum Status {
        PENDING(0, "审核中"),
        PUBLISHED(1, "已发布"),
        REMOVED(2, "已下架"),
        VIOLATED(3, "违规");

        private final Integer code;
        private final String desc;

        Status(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }

    /**
     * 公开属性枚举
     */
    public enum PublicType {
        PRIVATE(0, "私密"),
        PUBLIC(1, "公开");

        private final Integer code;
        private final String desc;

        PublicType(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public Integer getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }
}
