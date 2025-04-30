package com.clone.linkedin.post_service.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostLikedEvent {
    Long postId;
    Long creatorId;
    Long likedByUserId;
}
