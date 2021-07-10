package com.team.post;

import com.team.post.dto.output.SavePostOutput;
import com.team.post.dto.request.SavePostRequest;
import com.team.post.dto.response.SavePostResponse;
import com.team.post.util.ImageUploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<SavePostResponse> savePost(@Valid @ModelAttribute SavePostRequest request) {
        List<Long> postImageIds = saveImages(request);
        SavePostOutput output = postService.save(request.toInput(postImageIds));

        UriComponents uriComponents = MvcUriComponentsBuilder
                .fromMethodCall(on(PostController.class).savePost(request))
                .build();

        return ResponseEntity
                .created(uriComponents.toUri())
                .body(new SavePostResponse(output));
    }

    private List<Long> saveImages(SavePostRequest request) {
        List<PostImage> postImages = uploader.storeImages(request.getPostImages());
        return postImages.stream()
                .map(PostImage::getId)
                .collect(Collectors.toList());
    }
}