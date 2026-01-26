package com.nativc.funflow.mapper;

import com.nativc.funflow.entity.Video;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 视频数据访问接口
 */
@Mapper
public interface VideoMapper {

    /**
     * 插入新视频
     *
     * @param video 视频实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO `videos` (user_id, video_url, cover_url, title, is_public, status) " +
            "VALUES (#{userId}, #{videoUrl}, #{coverUrl}, #{title}, #{isPublic}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "videoId", keyColumn = "video_id")
    int insert(Video video);

    /**
     * 查询用户视频列表（分页，排除违规视频，按创建时间倒序）
     *
     * @param userId 用户ID
     * @param offset 偏移量
     * @param limit  每页数量
     * @return 视频列表
     */
    @Select("SELECT video_id, user_id, video_url, cover_url, title, view_count, like_count, " +
            "collect_count, comment_count, is_public, status, created_at " +
            "FROM `videos` " +
            "WHERE user_id = #{userId} AND status != 3 AND deleted_at IS NULL " +
            "ORDER BY created_at DESC " +
            "LIMIT #{offset}, #{limit}")
    List<Video> findByUserIdWithPagination(@Param("userId") Long userId,
                                           @Param("offset") int offset,
                                           @Param("limit") int limit);

    /**
     * 统计用户视频总数（排除违规视频）
     *
     * @param userId 用户ID
     * @return 视频总数
     */
    @Select("SELECT COUNT(*) FROM `videos` WHERE user_id = #{userId} AND status != 3 AND deleted_at IS NULL")
    int countByUserId(@Param("userId") Long userId);
}
