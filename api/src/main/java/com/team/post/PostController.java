package com.team.post;

import com.team.post.dto.output.SavePostOutput;
import com.team.post.dto.request.SavePostRequest;
import com.team.post.dto.response.SavePostResponse;
import com.team.post.util.ImageUploader;
import com.team.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final ImageUploader uploader;
    private final PostService postService;

    @PostMapping(value = "")
    public ResponseEntity<SavePostResponse> savePost(@CurrentUser Long userId, @Valid @ModelAttribute SavePostRequest request) {
        List<Long> postImageIds = saveImages(request);
        SavePostOutput output = postService.save(userId, request.toInput(postImageIds));

        UriComponents uriComponents = MvcUriComponentsBuilder
                .fromMethodCall(on(PostController.class).savePost(userId, request))
                .build();

        return ResponseEntity
                .created(uriComponents.toUri())
                .body(new SavePostResponse(output));
    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity<Void> deletePost(@PathVariable("post-id") Long postId) {
        postService.delete(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    private List<Long> saveImages(SavePostRequest request) {
        List<PostImage> postImages = uploader.storeImages(request.getPostImages());
        return postImages.stream()
                .map(PostImage::getId)
                .collect(Collectors.toList());
    }
}
