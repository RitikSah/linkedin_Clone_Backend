package com.clone.linkedin.post_service.service;

public interface PostLikeService {

    void likePost(Long postId);

    void unlikePost(Long postId);
}
