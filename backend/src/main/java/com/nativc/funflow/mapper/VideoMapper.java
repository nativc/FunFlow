package com.nativc.funflow.mapper;

import com.nativc.funflow.entity.Video;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

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
}
