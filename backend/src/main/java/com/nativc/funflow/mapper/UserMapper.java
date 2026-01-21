package com.nativc.funflow.mapper;

import com.nativc.funflow.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    /**
     * 根据邮箱查询用户是否存在
     *
     * @param email 邮箱地址（小写）
     * @return 用户数量
     */
    @Select("SELECT COUNT(1) FROM `users` WHERE email = #{email}")
    int countByEmail(@Param("email") String email);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱地址（小写）
     * @return 用户实体
     */
    @Select("SELECT user_id, password_hash, status FROM `users` WHERE email = #{email}")
    User findByEmail(@Param("email") String email);

    /**
     * 插入新用户
     *
     * @param user 用户实体
     * @return 影响的行数
     */
    @Insert("INSERT INTO `users` (email, password_hash, username, nickname, status) " +
            "VALUES (#{email}, #{passwordHash}, #{username}, #{nickname}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    int insert(User user);

    /**
     * 更新最后登录时间
     *
     * @param userId 用户ID
     */
    @Update("UPDATE `users` SET last_login_at = NOW() WHERE user_id = #{userId}")
    void updateLastLoginTime(@Param("userId") Long userId);

    /**
     * 根据用户ID查询用户信息（用于个人资料展示）
     *
     * @param userId 用户ID
     * @return 用户实体
     */
    @Select("SELECT user_id, username, nickname, avatar_url, bio, " +
            "following_count, follower_count, cached_total_likes_received " +
            "FROM `users` WHERE user_id = #{userId}")
    User findById(@Param("userId") Long userId);
}