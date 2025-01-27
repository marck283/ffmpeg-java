package it.disi.unitn.lasagna.audiocreator;

import it.disi.unitn.FFMpeg;
import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AudioExtractorTest {

    @Test
    public void extractAudio() throws InvalidArgumentException, IOException {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");
        AudioExtractor extractor = new AudioExtractor(builder, "./src/test/resources/output/mp3/000.wma",
                "./src/test/resources/input/mp4/000.wmv");
        extractor.createCommand();
        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(1L, TimeUnit.MINUTES, "./", null);
    }

}
