package com.team.dbutil;

import com.team.post.Post;
import com.team.post.PostLike;
import com.team.post.PostLikeRepository;
import com.team.user.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PostLikeData {
    private final PostLikeRepository postLikeRepository;

    public PostLikeData(PostLikeRepository postLikeRepository){
        this.postLikeRepository=postLikeRepository;
    }

    @Transactional
    public PostLike savePostLike(User user, Post post){
        PostLike postLike = createPostLike(user,post);
        return postLikeRepository.save(postLike);
    }

    private PostLike createPostLike(User user, Post post){
        return PostLike.builder()
                .post(post)
                .user(user)
                .build();
    }
}
