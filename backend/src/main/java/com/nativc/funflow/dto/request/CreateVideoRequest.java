package com.nativc.funflow.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 创建视频请求 DTO
 */
@Data
public class CreateVideoRequest {

    /**
     * 视频文件，最大 300 MB
     */
    @NotNull(message = "视频文件不能为空")
    private MultipartFile video;

    /**
     * 封面图片文件（可选），最大 5 MB
     */
    private MultipartFile cover;

    /**
     * 视频标题（文案）
     */
    @Size(max = 300, message = "标题最多300个字符")
    private String title;

    /**
     * 标签列表（JSON数组字符串）
     */
    @NotNull(message = "标签不能为空")
    private String tagsJson;

    /**
     * 是否公开：0-私密，1-公开
     */
    private Integer isPublic;
}
