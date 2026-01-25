package com.nativc.funflow.controller;

import com.nativc.funflow.common.Result;
import com.nativc.funflow.dto.request.CreateVideoRequest;
import com.nativc.funflow.exception.BusinessException;
import com.nativc.funflow.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 视频控制器
 * 视频相关接口
 */
@RestController
@RequestMapping("/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    /**
     * 创建视频作品
     *
     * @param video    视频文件
     * @param cover    封面图片文件（可选）
     * @param title    视频标题
     * @param tags     标签列表（JSON数组字符串）
     * @param isPublic 是否公开
     * @return 创建结果
     */
    @PostMapping
    public Result<Void> createVideo(
            @RequestParam("video") MultipartFile video,
            @RequestParam(value = "cover", required = false) MultipartFile cover,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam("tags") String tags,
            @RequestParam(value = "isPublic", required = false, defaultValue = "1") Integer isPublic) {

        // 构建请求对象
        CreateVideoRequest request = new CreateVideoRequest();
        request.setVideo(video);
        request.setCover(cover);
        request.setTitle(title);
        request.setTagsJson(tags);
        request.setIsPublic(isPublic);

        videoService.createVideo(request);

        return Result.success();
    }
}
