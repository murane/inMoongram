package com.team.post;

import com.team.post.dto.input.PostLikeCreateInput;
import com.team.post.dto.input.PostLikeInfoInput;
import com.team.user.User;
import com.team.user.UserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostLikeServiceTest {

    @Mock
    private PostLikeRepository postLikeRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserService userService;

    @InjectMocks
    private PostLikeService postLikeService;

    @Test
    void 좋아요생성(){
        User authorUser = User.builder()
                .id(1L)
                .name("testAuthor")
                .nickname("imAuthor")
                .email("test@email.com")
                .password("testpw1234")
                .build();

        User likeUser = User.builder()
                .id(2L)
                .name("testlikeuser")
                .nickname("imlikeuser")
                .email("test2@email.com")
                .password("testpw1234")
                .build();
        Post post = new Post("content1",authorUser);
        post.setIdForTest(3L);
        PostLikeCreateInput postLikeCreateInput =
                new PostLikeCreateInput(likeUser.getId(), post.getId());
        PostLike postLike = PostLike.builder()
                .id(4L)
                .user(likeUser)
                .post(post)
                .build();

        given(userService.findUserById(anyLong())).willReturn(likeUser);
        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));
        given(postLikeRepository.save(any())).willReturn(postLike);
        var output = postLikeService.createLike(postLikeCreateInput);

        assertThat(output.getPostLikeId()).isEqualTo(postLike.getId());
    }

    @Test
    @Disabled
    void 좋아요취소(){

    }

    @Test
    void 좋아요조회(){
        User authorUser = User.builder()
                .id(1L)
                .name("testAuthor")
                .nickname("imAuthor")
                .email("test@email.com")
                .password("testpw1234")
                .build();
        User likeUser1 = User.builder()
                .id(2L)
                .name("testlikeuser1")
                .nickname("imlikeuser1")
                .email("test1@email.com")
                .password("testpw1234")
                .build();
        User likeUser2 = User.builder()
                .id(3L)
                .name("testlikeuser2")
                .nickname("imlikeuser2")
                .email("test2@email.com")
                .password("testpw1234")
                .build();
        Post post = new Post("content1",authorUser);

        PostLike postLike1 = PostLike.builder()
                .id(5L)
                .user(likeUser1)
                .post(post)
                .build();
        PostLike postLike2 = PostLike.builder()
                .id(6L)
                .user(likeUser2)
                .post(post)
                .build();

        post.setIdForTest(4L);
        PostLikeInfoInput postLikeInfoInput = new PostLikeInfoInput(post.getId());

        given(postRepository.findById(anyLong())).willReturn(Optional.of(post));

        var output = postLikeService.getPostLikeList(postLikeInfoInput);

        assertThat(output.getPostLikeInfoList().size()).isEqualTo(2);
    }
}
