package com.team.user;

import com.team.dbutil.DatabaseCleanup;
import com.team.dbutil.FollowData;
import com.team.dbutil.UserData;
import com.team.user.dto.response.FollowerInfoListResponse;
import com.team.user.dto.response.FollowerInfoResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleanup dbCleanup;

    @Autowired
    private UserData userData;

    @Autowired
    private FollowData followData;

    @AfterEach
    void tearDown() {
        dbCleanup.execute();
    }

    @Test
    @DisplayName("팔로워 리스트 가져오기")
    void getFollowerList() {
        User user1 = userData.saveUser("승화", "a", "a@naver.com");
        User user2 = userData.saveUser("준수", "b", "b@naver.com");
        User user3 = userData.saveUser("용우", "c", "c@naver.com");
        User user4 = userData.saveUser("용우1", "c1", "c1@naver.com");
        Follow follow1 = followData.saveFollow(user2, user1);
        Follow follow2 = followData.saveFollow(user3, user1);
        Follow follow3 = followData.saveFollow(user4, user1);

        List<FollowerInfoResponse> actual = getFollowerTest(user1.getId());
        actual.sort(Comparator.comparingLong(FollowerInfoResponse::getUserId));
        List<User> expected = Arrays.asList(user2, user3, user4);
        Assertions.assertThat(actual.size()).isEqualTo(expected.size());
        for(int i = 0; i < actual.size(); i++) {
            Assertions.assertThat(actual.get(i).getUserId()).isEqualTo(expected.get(i).getId());
        }
    }

    @Test
    @DisplayName("맞팔로우 확인")
    void followBackCheck() {
        User user1 = userData.saveUser("승화", "a", "a@naver.com");
        User user2 = userData.saveUser("준수", "b", "b@naver.com");
        User user3 = userData.saveUser("용우", "c", "c@naver.com");
        Follow follow1 = followData.saveFollow(user2, user1);
        Follow follow2 = followData.saveFollow(user3, user1);
        Follow follow3 = followData.saveFollow(user1, user2);

        List<FollowerInfoResponse> actual = getFollowerTest(user1.getId());
        actual.sort(Comparator.comparingLong(FollowerInfoResponse::getUserId));
        List<User> expected = Arrays.asList(user2);
        Assertions.assertThat(actual.size()).isEqualTo(2);
        for (FollowerInfoResponse followerInfoResponse : actual) {
            if (followerInfoResponse.isFollowBack()) {
                Assertions.assertThat(followerInfoResponse.getUserId()).isEqualTo(user2.getId());
            }
        }
    }

    List<FollowerInfoResponse> getFollowerTest(Long id) {
        Response response =
                given()
                        .port(port)
                        .param("user-id", id)
                .when()
                        .get("user/follower/list")
                .thenReturn();

        Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
        return response.getBody()
                .as(FollowerInfoListResponse.class)
                .getFollowerInfoResponses();
    }


}