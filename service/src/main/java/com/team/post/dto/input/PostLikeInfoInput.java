package com.team.post.dto.input;

import lombok.Data;

@Data
public class PostLikeInfoInput {
    private Long postId;

    public PostLikeInfoInput(Long postId) {
        this.postId = postId;
    }
}
