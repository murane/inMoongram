package com.team.post;

import com.team.post.dto.input.PostLikeCreateInput;
import com.team.post.dto.input.PostLikeInfoInput;
import com.team.post.dto.response.PostLikeCreateResponse;
import com.team.post.dto.response.PostLikeInfoResponse;
import com.team.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RequiredArgsConstructor
@RestController
@RequestMapping("/post")
public class PostLikeController {
    private final PostLikeService postLikeService;

    @PostMapping("/{post-id}/like")
    public ResponseEntity<PostLikeCreateResponse>
    createLike(@PathVariable("post-id") Long postId, @CurrentUser Long userId) {
        var output = postLikeService.createLike(
                new PostLikeCreateInput(userId, postId)
        );

        UriComponents uriComponents = MvcUriComponentsBuilder
                .fromMethodCall(on(PostLikeController.class)
                        .createLike(postId, userId))
                .build();
        return ResponseEntity
                .created(uriComponents.toUri())
                .body(new PostLikeCreateResponse(output));
    }

    @DeleteMapping("{postId}/like/{postLikeId}")
    public ResponseEntity<Void> cancelLike(@PathVariable("postId") Long postId
            , @PathVariable("postLikeId") Long postLikeId) {

        postLikeService.cancelLike(postLikeId);

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("{postId}/likes")
    public ResponseEntity<PostLikeInfoResponse> getLikes(@PathVariable("postId") Long postId) {
        var output = postLikeService.getPostLikeList(
                new PostLikeInfoInput(postId)
        );
        return ResponseEntity
                .ok(new PostLikeInfoResponse(output));
    }
}
