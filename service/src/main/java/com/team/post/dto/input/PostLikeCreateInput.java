package com.team.post.dto.input;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeCreateInput {
    private Long userId;
    private Long postId;

    public PostLikeCreateInput(Long userId, Long postId) {
        this.userId = userId;
        this.postId = postId;
    }
}
