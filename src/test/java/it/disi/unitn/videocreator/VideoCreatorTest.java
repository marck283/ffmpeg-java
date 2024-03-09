package it.disi.unitn.videocreator;

import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.NotEnoughArgumentsException;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

class VideoCreatorTest {

    @Test
    void createCommand() throws NotEnoughArgumentsException, InvalidArgumentException, UnsupportedOperatingSystemException,
            IOException {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");
        VideoCreator creator = builder.newVideoCreator("./src/test/resources/input/mp4/example.mp4",
                "./src/test/resources/input/images", "000.jpeg");
        creator.setVideoSize(800, 600);
        creator.setCodecID("mjpeg");
        creator.setPixelFormat("yuv420p");
        creator.setOutFullRange(true); //If using mjpeg and YUV pixel formats, we have to set the color range to full.
        creator.setVideoQuality(18);
        creator.createCommand(30L, TimeUnit.SECONDS);
    }
}