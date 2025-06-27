package it.disi.unitn.videocreator;

import it.disi.unitn.FFMpeg;
import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class VideoExtractorTest {

    @Test
    public void extractVideo() throws InvalidArgumentException, IOException {
        FFMpegBuilder builder = new FFMpegBuilder();
        VideoExtractor extractor = new VideoExtractor(builder, "./src/test/resources/output/mp4/000.wmv",
                "./src/test/resources/input/mp4/000.wmv");
        extractor.createCommand();
        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(1L, TimeUnit.MINUTES, "./", null);
    }

}
