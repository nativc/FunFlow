package com.nativc.funflow.service;

import com.nativc.funflow.dto.request.CreateVideoRequest;
import com.nativc.funflow.dto.response.UserVideosResponse;

/**
 * 视频服务接口
 */
public interface VideoService {

    /**
     * 创建视频作品
     *
     * @param request 创建视频请求
     */
    void createVideo(CreateVideoRequest request);

    /**
     * 获取当前用户作品列表
     *
     * @param page     页码，从1开始
     * @param pageSize 每页数量
     * @return 用户作品列表响应
     */
    UserVideosResponse getUserVideos(Integer page, Integer pageSize);
}
