package com.nativc.funflow.mapper;

import com.nativc.funflow.entity.Tag;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 标签数据访问接口
 */
@Mapper
public interface TagMapper {

    /**
     * 根据标签名称查询标签
     *
     * @param tagName 标签名称
     * @return 标签实体
     */
    @Select("SELECT tag_id, tag_name, use_count, created_at FROM `tags` WHERE tag_name = #{tagName}")
    Tag findByTagName(@Param("tagName") String tagName);

    /**
     * 插入新标签
     *
     * @param tag 标签实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO `tags` (tag_name, use_count) VALUES (#{tagName}, #{useCount})")
    @Options(useGeneratedKeys = true, keyProperty = "tagId", keyColumn = "tag_id")
    int insert(Tag tag);

    /**
     * 增加标签引用次数
     *
     * @param tagId 标签ID
     * @return 影响的行数
     */
    @Update("UPDATE `tags` SET use_count = use_count + 1 WHERE tag_id = #{tagId}")
    int incrementUseCount(@Param("tagId") Long tagId);
}
