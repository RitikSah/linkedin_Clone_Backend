package com.clone.linkedin.post_service.service;

import com.clone.linkedin.post_service.auth.UserContextHolder;
import com.clone.linkedin.post_service.entity.Post;
import com.clone.linkedin.post_service.entity.PostLike;
import com.clone.linkedin.post_service.event.PostLikedEvent;
import com.clone.linkedin.post_service.exception.BadRequestException;
import com.clone.linkedin.post_service.exception.ResourceNotFoundException;
import com.clone.linkedin.post_service.repository.PostLikeRepository;
import com.clone.linkedin.post_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeServiceImpl implements PostLikeService{

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    private final KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;

    @Override
    public void likePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Attempting to like the post with id: {}" , postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId,postId);
        if(alreadyLiked) throw new BadRequestException("Cannot like the same post again.");

        PostLike postLike = PostLike.builder()
                .postId(postId)
                .userId(userId)
                .build();

        postLikeRepository.save(postLike);
        log.info("Post with id: {} liked successfully" , postId);

        PostLikedEvent postLikedEvent = PostLikedEvent.builder()
                .postId(postId)
                .creatorId(post.getUserId())
                .likedByUserId(userId)
                .build();

        kafkaTemplate.send("post-liked-topic", postId, postLikedEvent);
    }

    @Override
    public void unlikePost(Long postId) {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Attempting to unlike the post with id: {}" , postId);

        boolean exists = postRepository.existsById(postId);
        if(!exists) throw new ResourceNotFoundException("Post not found with id: " + postId);

        boolean alreadyLiked = postLikeRepository.existsByUserIdAndPostId(userId,postId);
        if(!alreadyLiked) throw new BadRequestException("Cannot unlike the post which is not liked.");

        postLikeRepository.deleteByUserIdAndPostId(userId,postId);
        log.info("Post with id: {} unliked successfully" , postId);
    }
}
