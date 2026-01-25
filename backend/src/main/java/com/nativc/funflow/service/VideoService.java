package com.nativc.funflow.service;

import com.nativc.funflow.dto.request.CreateVideoRequest;

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
}
