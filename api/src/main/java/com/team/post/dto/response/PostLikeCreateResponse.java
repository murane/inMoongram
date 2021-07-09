package com.team.post.dto.response;

import com.team.post.dto.output.PostLikeCreateOutput;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeCreateResponse {
    private Long postLikeId;

    public PostLikeCreateResponse(Long postLikeId) {
        this.postLikeId = postLikeId;
    }

    public PostLikeCreateResponse(PostLikeCreateOutput output){
        this.postLikeId= output.getPostLikeId();
    }
}
