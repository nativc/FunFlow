package com.nativc.funflow.mapper;

import com.nativc.funflow.entity.VideoTag;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * 视频标签关联数据访问接口
 */
@Mapper
public interface VideoTagMapper {

    /**
     * 插入视频标签关联
     *
     * @param videoTag 视频标签关联实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO `video_tags` (video_id, tag_id) VALUES (#{videoId}, #{tagId})")
    int insert(VideoTag videoTag);
}
