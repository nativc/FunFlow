package com.nativc.funflow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用户作品列表响应 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVideosResponse {

    /**
     * 用户 ID，标识当前作品列表所属用户
     */
    private Long userId;

    /**
     * 作品总数（不包含违规视频）
     */
    private Integer total;

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页数量
     */
    private Integer pageSize;

    /**
     * 作品列表，按创建时间倒序排序
     */
    private List<VideoItem> videos;
}
