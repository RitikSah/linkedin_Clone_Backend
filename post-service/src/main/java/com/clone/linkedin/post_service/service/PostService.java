package com.clone.linkedin.post_service.service;

import com.clone.linkedin.post_service.dto.PostCreateRequestDto;
import com.clone.linkedin.post_service.dto.PostDto;

import java.util.List;


public interface PostService {
    PostDto createPost(PostCreateRequestDto postCreateRequestDto, long userId);

    PostDto getPostById(Long postId);

    List<PostDto> getAllPostsOfUser(Long userId);
}
