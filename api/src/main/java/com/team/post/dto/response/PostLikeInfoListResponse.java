package com.team.post.dto.response;

import com.team.post.dto.output.PostLikeInfoOutput;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLikeInfoListResponse {
    private List<PostLikeInfo> postLikes;

    public PostLikeInfoListResponse(List<PostLikeInfo> postLikes) {
        this.postLikes = postLikes;
    }

    public PostLikeInfoListResponse(PostLikeInfoOutput output) {
        this.postLikes = output.getPostLikeInfoList().stream()
            .map(
                    it-> PostLikeInfo.builder()
                    .postId(it.getPostId())
                    .postLikeId(it.getPostLikeId())
                    .userId(it.getUserId())
                    .build()
            ).collect(Collectors.toList());
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostLikeInfo {
        private Long postLikeId;
        private Long postId;
        private Long userId;

        @Builder
        public PostLikeInfo(Long postLikeId, Long postId, Long userId) {
            this.postLikeId = postLikeId;
            this.postId = postId;
            this.userId = userId;
        }
    }
}
