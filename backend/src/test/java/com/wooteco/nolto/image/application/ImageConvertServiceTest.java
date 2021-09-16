package com.wooteco.nolto.image.application;

import com.wooteco.nolto.image.config.FFmpegConfig;
import com.wooteco.nolto.image.infrastructure.FFmpegConverter;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFprobe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@SpringBootTest(classes = {ImageConvertService.class, FFmpegConfig.class, FFmpegConverter.class})
class ImageConvertServiceTest {

    private final String gifFileName = "jjv1FK.gif";

    @Autowired
    private ImageConvertService imageConvertService;

    @MockBean
    private FFmpegConverter ffmpegConverter;

    @MockBean
    private FFmpegConfig ffmpegConfig;

    @BeforeEach
    void setUp() throws IOException {
        given(ffmpegConfig.ffmpeg()).willReturn(new FFmpeg());
        given(ffmpegConfig.ffprobe()).willReturn(new FFprobe());
        willDoNothing().given(ffmpegConverter).convertGifToMp4(anyString(), anyString());
    }

    @Test
    void convertGifToMp4() {
        // given
        URL resource = getClass().getClassLoader().getResource("static/" + gifFileName);
        String gifFilePath = resource.getPath();
        File gifFile = new File(gifFilePath);

        // when
        File mp4File = imageConvertService.convertGifToMp4(gifFile);

        // then
        String mp4FileName = mp4File.getName();
        String expectedFileName = "jjv1FK.mp4";
        assertThat(mp4FileName).isEqualTo(expectedFileName);
    }
}