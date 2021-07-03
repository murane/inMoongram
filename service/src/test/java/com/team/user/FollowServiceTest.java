package com.team.user;

import com.team.user.dto.output.FollowListOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {
    @Mock
    private FollowRepository followRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private FollowService followService;

    private User user1;
    private User user2;
    private User user3;

    private Follow follow1;
    private Follow follow2;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .name("testUser1")
                .nickname("testNickname1")
                .email("test1@test.com")
                .password("testPassword1")
                .build();
        user2 = User.builder()
                .name("testUser2")
                .nickname("testNickname2")
                .email("test2@test.com")
                .password("testPassword2")
                .build();
        user3 = User.builder()
                .name("testUser3")
                .nickname("testNickname3")
                .email("test3@test.com")
                .password("testPassword3")
                .build();

        follow1 = Follow.builder()
                .follower(user1)
                .followee(user2)
                .build();
        follow2 = Follow.builder()
                .follower(user1)
                .followee(user3)
                .build();
    }

    @Test
    void 팔로우_취소() {
        given(userService.findByNickname(user1.getNickname())).willReturn(user1);
        given(userService.findByNickname(user2.getNickname())).willReturn(user2);
        given(followRepository.findById(any())).willReturn(Optional.of(follow1));

        int followerCount = user1.getFollowees().size();
        int followeeCount = user2.getFollowers().size();

        followService.unfollow(1L);

        assertThat(user1.getFollowees().size()).isEqualTo(followerCount - 1);
        assertThat(user2.getFollowers().size()).isEqualTo(followeeCount - 1);
    }
}