package com.clone.linkedin.post_service.repository;

import com.clone.linkedin.post_service.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike,Long>{
    boolean existsByUserIdAndPostId(long userId, Long postId);

    @Transactional
    void deleteByUserIdAndPostId(Long userId, Long postId);
}
