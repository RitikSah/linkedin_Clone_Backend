package com.clone.linkedin.post_service.service;

import com.clone.linkedin.post_service.auth.UserContextHolder;
import com.clone.linkedin.post_service.dto.PostCreateRequestDto;
import com.clone.linkedin.post_service.dto.PostDto;
import com.clone.linkedin.post_service.entity.Post;
import com.clone.linkedin.post_service.event.PostCreatedEvent;
import com.clone.linkedin.post_service.exception.ResourceNotFoundException;
import com.clone.linkedin.post_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostsServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;

    public PostDto createPost(PostCreateRequestDto postCreateRequestDto) {
        Long userId = UserContextHolder.getCurrentUserId();
        Post post = modelMapper.map(postCreateRequestDto,Post.class);
        post.setUserId(userId);

        Post savedPost = postRepository.save(post);
        PostCreatedEvent postCreatedEvent = PostCreatedEvent.builder()
                .postId(savedPost.getId())
                .creatorId(userId)
                .content(savedPost.getContent())
                .build();

        kafkaTemplate.send("post-createfd-topic", postCreatedEvent);
        return modelMapper.map(savedPost,PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.debug("Retrieving post with ID : {}" , postId);

        Post post = postRepository.findById(postId).orElseThrow(() ->
                new ResourceNotFoundException("Post not found with id: " + postId));

        return modelMapper.map(post,PostDto.class);
    }

    @Override
    public List<PostDto> getAllPostsOfUser(Long userId) {
        List<Post> postList = postRepository.findByUserId(userId);
        return postList.stream()
                .map((element) -> modelMapper.map(element , PostDto.class))
                .toList();
    }
}
