package com.wooteco.nolto.acceptance;

import com.wooteco.nolto.auth.ui.dto.TokenResponse;
import com.wooteco.nolto.exception.dto.ExceptionResponse;
import com.wooteco.nolto.user.domain.User;
import com.wooteco.nolto.user.ui.dto.MemberHistoryResponse;
import com.wooteco.nolto.user.ui.dto.MemberResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.wooteco.nolto.acceptance.FeedAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("회원 관련 기능")
public class UserAcceptanceTest extends AcceptanceTest {

    @DisplayName("로그인 된 사용자라면 회원 정보를 받아올 수 있다.")
    @Test
    void getMemberInfoWithToken() {
        //given
        TokenResponse userToken = 가입된_유저의_토큰을_받는다();

        //when
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(userToken);

        //then
        알맞은_회원_정보_조회됨(response, 엄청난_유저);
    }

    @DisplayName("로그인 되지 않은 사용자라면 회원 정보를 받아올 수 없다.")
    @Test
    void cannotGetMemberInfoWithoutToken() {
        //when
        ExtractableResponse<Response> response = 토큰_없이_회원_정보_요청();

        //then
        토큰_필요_예외_발생(response);
    }
/*
    @DisplayName("사용자의 히스토리(좋아요 한 글, 내가 작성한 글, 내가 남긴 댓글)를 조회할 수 있다.")
    @Test
    void getUserHistory() {
        //given
        TokenResponse userToken1 = 가입된_유저의_토큰을_받는다();
        TokenResponse userToken2 = 가입된_유저의_토큰을_받는다(엄청난_유저2);

        ExtractableResponse<Response> saveResponse1 = 피드를_작성한다(진행중_단계의_피드_요청, userToken1.getAccessToken());
        Long feedId1 = Long.valueOf(saveResponse1.header("Location").replace("/feeds/", ""));

        ExtractableResponse<Response> saveResponse2 = 피드를_작성한다(전시중_단계의_피드_요청, userToken2.getAccessToken());
        Long feedId2 = Long.valueOf(saveResponse2.header("Location").replace("/feeds/", ""));

        좋아요를_누른다(userToken1.getAccessToken(), feedId1);
        좋아요를_누른다(userToken1.getAccessToken(), feedId2);

        //when
        ExtractableResponse<Response> response = 내_히스토리_조회_요청(userToken1);

        //then
        알맞은_회원_히스토리_조회됨(response, 엄청난_유저);
    }
*/
    public ExtractableResponse<Response> 내_회원_정보_조회_요청(TokenResponse tokenResponse) {
        return given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    public void 알맞은_회원_정보_조회됨(ExtractableResponse<Response> response, User expectedUser) {
        MemberResponse memberResponse = response.as(MemberResponse.class);
        assertThat(memberResponse.getId()).isNotNull();
        assertThat(memberResponse.getSocialType()).isEqualTo(expectedUser.getSocialType().name());
        assertThat(memberResponse.getNickName()).isEqualTo(expectedUser.getNickName());
        assertThat(memberResponse.getImageUrl()).isEqualTo(expectedUser.getImageUrl());
    }

    public ExtractableResponse<Response> 토큰_없이_회원_정보_요청() {
        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me")
                .then()
                .log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    public void 토큰_필요_예외_발생(ExtractableResponse<Response> response) {
        ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getErrorCode()).isEqualTo("auth-002");
        assertThat(exceptionResponse.getMessage()).isEqualTo("토큰이 필요합니다.");
    }

    private ExtractableResponse<Response> 내_히스토리_조회_요청(TokenResponse tokenResponse) {
        return given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/members/me/history")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    private void 알맞은_회원_히스토리_조회됨(ExtractableResponse<Response> response, User expectedUser) {
        MemberHistoryResponse memberHistoryResponse = response.as(MemberHistoryResponse.class);
        assertThat(memberHistoryResponse.getLikedFeeds()).isNotNull();
        assertThat(memberHistoryResponse.getMyFeeds()).isNotNull();
        assertThat(memberHistoryResponse.getMyComments()).isNotNull();
    }
}
