package com.wooteco.nolto.feed.domain;

import com.wooteco.nolto.exception.BadRequestException;
import com.wooteco.nolto.exception.ErrorType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.wooteco.nolto.UserFixture.아마찌_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FeedTest {

    private Feed feed;

    @BeforeEach
    void setUp() {
        feed = Feed.builder()
                .title("아마찌의 개쩌는 지하철 미션")
                .content("난 너무 잘해")
                .step(Step.PROGRESS)
                .isSos(true)
                .storageUrl("https://github.com/woowacourse-teams/2021-nolto")
                .deployedUrl("https://github.com/woowacourse-teams/2021-nolto")
                .thumbnailUrl("https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png")
                .build();
    }

    @DisplayName("피드에 작성자 추가할 수 있다.")
    @Test
    void writtenBy() {
        // when
        Feed feed = this.feed.writtenBy(아마찌_생성());

        // then
        assertThat(feed.getAuthor()).isEqualTo(아마찌_생성());
    }

    @DisplayName("조회하지 않은 경우 조회수를 증가시킨다.")
    @Test
    void increaseView() {
        // when
        int beforeView = feed.getViews();
        feed.increaseView(false);

        // then
        assertThat(feed.getViews()).isEqualTo(beforeView + 1);
    }

    @DisplayName("이미 조회했을 시 조회수를 증가시키지 않는다.")
    @Test
    void notIncreaseView() {
        // when
        int beforeView = feed.getViews();
        feed.increaseView(true);

        // then
        assertThat(feed.getViews()).isEqualTo(beforeView);
    }

    @DisplayName("전시중(완료된) 프로젝트가의 배포 URL가 null이거나 공백인 경우 예외가 발생한다.")
    @Test
    void mustHaveDeployUrlWhenCompleteStep() {
        assertThatThrownBy(() ->
                Feed.builder()
                        .title("아마찌의 개쩌는 지하철 미션")
                        .content("난 너무 잘해")
                        .step(Step.COMPLETE)
                        .isSos(true)
                        .storageUrl("https://github.com/woowacourse-teams/2021-nolto")
                        .thumbnailUrl("https://dksykemwl00pf.cloudfront.net/nolto-default-thumbnail.png")
                        .build())
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ErrorType.MISSING_DEPLOY_URL.getMessage());
    }
}
