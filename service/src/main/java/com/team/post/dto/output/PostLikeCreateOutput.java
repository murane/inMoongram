package com.team.post.dto.output;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeCreateOutput {
    private Long postLikeId;

    public PostLikeCreateOutput(Long postLikeId) {
        this.postLikeId = postLikeId;
    }
}
