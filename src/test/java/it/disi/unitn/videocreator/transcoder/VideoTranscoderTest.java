package it.disi.unitn.videocreator.transcoder;

import it.disi.unitn.FFMpeg;
import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.NotEnoughArgumentsException;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

class VideoTranscoderTest {

    @Test
    void createCommand() throws NotEnoughArgumentsException, IOException, InvalidArgumentException, UnsupportedOperatingSystemException {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");
        VideoTranscoder transcoder = builder.newVideoTranscoder("./src/test/resources/input/mp4/example.mov",
                "./src/test/resources/input/mp4", "example.mp4");
        transcoder.enableVideoExtraction();
        transcoder.setVideoSize(800, 600, "yuvj420p", true);
        transcoder.setCodecID("mjpeg", true);
        transcoder.setPixelFormat("yuv420p");
        transcoder.setOutFullRange(true); //If using mjpeg and YUV pixel formats, we have to set the color range to full.
        transcoder.setVideoQuality(18);
        transcoder.createCommand(/*30L, TimeUnit.MINUTES*/);
        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(30L, TimeUnit.MINUTES);
    }

    @Test
    void createCommandForAudio() throws NotEnoughArgumentsException, InvalidArgumentException, IOException {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");
        VideoTranscoder transcoder = builder.newVideoTranscoder("./src/test/resources/input/mp4/002.wma",
                "./src/test/resources/input/mp4", "002.wmv");
        transcoder.enableAudioExtraction();
        transcoder.createCommand(/*30L, TimeUnit.MINUTES*/);
        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(30L, TimeUnit.MINUTES);
    }
}