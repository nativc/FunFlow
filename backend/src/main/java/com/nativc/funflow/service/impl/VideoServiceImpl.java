package com.nativc.funflow.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nativc.funflow.common.UserContext;
import com.nativc.funflow.dto.request.CreateVideoRequest;
import com.nativc.funflow.dto.response.UserVideosResponse;
import com.nativc.funflow.dto.response.VideoItem;
import com.nativc.funflow.entity.Tag;
import com.nativc.funflow.entity.Video;
import com.nativc.funflow.entity.VideoTag;
import com.nativc.funflow.exception.BusinessException;
import com.nativc.funflow.mapper.TagMapper;
import com.nativc.funflow.mapper.VideoMapper;
import com.nativc.funflow.mapper.VideoTagMapper;
import com.nativc.funflow.service.OssService;
import com.nativc.funflow.service.VideoService;
import com.nativc.funflow.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 视频服务实现类
 */
@Slf4j
@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private VideoTagMapper videoTagMapper;

    @Autowired
    private OssService ossService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final List<String> ALLOWED_VIDEO_TYPES = Arrays.asList(
            "video/mp4", "video/quicktime", "video/x-msvideo"
    );

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/png", "image/jpg"
    );

    private static final long MAX_VIDEO_SIZE = 300 * 1024 * 1024L; // 300MB
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024L; // 5MB

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createVideo(CreateVideoRequest request) {
        // 1. 获取当前用户ID
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException("用户未登录");
        }

        // 2. 校验视频文件
        MultipartFile videoFile = request.getVideo();
        validateVideoFile(videoFile);

        // 3. 校验封面文件（如果有）
        MultipartFile coverFile = request.getCover();
        if (coverFile != null && !coverFile.isEmpty()) {
            validateCoverFile(coverFile);
        }

        // 4. 解析并校验标签
        List<String> tags = parseAndValidateTags(request.getTagsJson());

        // 5. 上传视频文件到OSS
        String videoUrl = uploadVideo(videoFile, userId);

        // 6. 上传封面文件到OSS（如果有）
        String coverUrl = null;
        if (coverFile != null && !coverFile.isEmpty()) {
            coverUrl = uploadCover(coverFile, userId);
        }

        // 7. 设置默认值
        Integer isPublic = request.getIsPublic() != null ? request.getIsPublic() : Video.PublicType.PUBLIC.getCode();
        String title = request.getTitle() != null ? request.getTitle() : "";

        // 8. 创建视频记录
        Video video = Video.builder()
                .userId(userId)
                .videoUrl(videoUrl)
                .coverUrl(coverUrl)
                .title(title)
                .isPublic(isPublic)
                .status(Video.Status.PENDING.getCode()) // 默认审核中
                .build();

        videoMapper.insert(video);
        Long videoId = video.getVideoId();

        log.info("视频创建成功, userId: {}, videoId: {}, videoUrl: {}", userId, videoId, videoUrl);

        // 9. 审核 + 处理标签
        // TODO: 审核流程实现后，标签关联应在审核通过时处理
        for (String tagName : tags) {
            // 查询或创建标签
            Tag tag = tagMapper.findByTagName(tagName);
            if (tag == null) {
                tag = Tag.builder()
                        .tagName(tagName)
                        .useCount(0)
                        .build();
                tagMapper.insert(tag);
                // tag.getTagId() MyBatis 自动回填
                log.info("标签创建成功, tagId: {}, tagName: {}", tag.getTagId(), tagName);
            }

            // 创建视频标签关联
            VideoTag videoTag = VideoTag.builder()
                    .videoId(videoId)
                    .tagId(tag.getTagId())
                    .build();
            videoTagMapper.insert(videoTag);

            // 标签引用数 + 1
            tagMapper.incrementUseCount(tag.getTagId());
        }
    }

    @Override
    public UserVideosResponse getUserVideos(Integer page, Integer pageSize) {
        // 1. 获取当前用户ID
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BusinessException("用户未登录");
        }

        // 2. 参数校验与默认值处理
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 20;
        }
        if (pageSize > 50) {
            pageSize = 50;
        }

        // 3. 计算偏移量
        int offset = (page - 1) * pageSize;

        // 4. 查询用户视频总数
        int total = videoMapper.countByUserId(userId);

        // 5. 查询视频列表
        List<Video> videos = videoMapper.findByUserIdWithPagination(userId, offset, pageSize);

        // 6. 转换为响应DTO
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        List<VideoItem> videoItems = new ArrayList<>();

        for (Video video : videos) {
            // 查询视频标签
            List<String> tags = videoTagMapper.findTagNamesByVideoId(video.getVideoId());

            VideoItem item = VideoItem.builder()
                    .videoId(video.getVideoId())
                    .title(video.getTitle())
                    .coverUrl(video.getCoverUrl())
                    .videoUrl(video.getVideoUrl())
                    .tags(tags)
                    .viewCount(video.getViewCount())
                    .likeCount(video.getLikeCount())
                    .status(video.getStatus())
                    .isPublic(video.getIsPublic())
                    .createTime(video.getCreatedAt() != null ? video.getCreatedAt().format(formatter) : null)
                    .build();

            videoItems.add(item);
        }

        // 7. 构建响应
        return UserVideosResponse.builder()
                .userId(userId)
                .total(total)
                .page(page)
                .pageSize(pageSize)
                .videos(videoItems)
                .build();
    }

    /**
     * 校验视频文件
     */
    private void validateVideoFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("视频文件不能为空");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_VIDEO_TYPES.contains(contentType)) {
            throw new BusinessException("不支持的视频格式，仅支持 mp4/mov/avi");
        }

        if (file.getSize() > MAX_VIDEO_SIZE) {
            throw new BusinessException("视频文件大小不能超过 300MB");
        }
    }

    /**
     * 校验封面文件
     */
    private void validateCoverFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new BusinessException("不支持的封面格式，仅支持 jpg/png/jpeg");
        }

        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new BusinessException("封面文件大小不能超过 5MB");
        }
    }

    /**
     * 解析并校验标签JSON数组
     */
    private List<String> parseAndValidateTags(String tagsJson) {
        try {
            // 解析标签JSON数组
            List<String> tagList = objectMapper.readValue(tagsJson, new TypeReference<List<String>>() {});

            // 校验标签数量
            if (tagList == null || tagList.isEmpty()) {
                throw new BusinessException("标签不能为空");
            }
            if (tagList.size() > 5) {
                throw new BusinessException("标签数量不能超过5个");
            }

            return tagList;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("解析标签JSON失败: {}", tagsJson, e);
            throw new BusinessException("标签格式错误，应为JSON数组格式");
        }
    }

    /**
     * 上传视频到OSS
     */
    private String uploadVideo(MultipartFile file, Long userId) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = FileUtil.getFileExtension(originalFilename);
            String filename = UUID.randomUUID().toString() + extension;
            String ossPath = String.format("video/%d/%s", userId, filename);

            String url = ossService.uploadFile(file, ossPath);
            log.info("视频上传成功, userId: {}, url: {}", userId, url);
            return url;
        } catch (Exception e) {
            log.error("视频上传失败, userId: {}", userId, e);
            throw new BusinessException("视频上传失败，请稍后重试");
        }
    }

    /**
     * 上传封面到OSS
     */
    private String uploadCover(MultipartFile file, Long userId) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = FileUtil.getFileExtension(originalFilename);
            String filename = UUID.randomUUID().toString() + extension;
            String ossPath = String.format("video_cover/%d/%s", userId, filename);

            String url = ossService.uploadFile(file, ossPath);
            log.info("封面上传成功, userId: {}, url: {}", userId, url);
            return url;
        } catch (Exception e) {
            log.error("封面上传失败, userId: {}", userId, e);
            throw new BusinessException("封面上传失败，请稍后重试");
        }
    }
}
