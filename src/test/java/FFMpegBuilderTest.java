import it.disi.unitn.FFMpeg;
import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.NotEnoughArgumentsException;
import it.disi.unitn.videocreator.VideoCreator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

class FFMpegBuilderTest {

    @Test
    void setVideoFrameSize() throws NotEnoughArgumentsException, IOException, InvalidArgumentException {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");
        VideoCreator vc = builder.newVideoCreator("./src/test/resources/input/mp4/000-1.mp4",
                "./src/test/resources/input/mp4", "000.mp4");
        builder.addInput("./src/test/resources/input/mp4/000.mp4");
        vc.setVideoSize(250, 200);
        builder.addOutput("./src/test/resources/input/mp4/000-1.mp4");
        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(1L, TimeUnit.MINUTES);
    }

    @Test
    void testSetVideoFrameSize() throws NotEnoughArgumentsException, IOException, InvalidArgumentException {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");
        VideoCreator vc = builder.newVideoCreator("./src/test/resources/input/mp4/000-1.mp4",
                "./src/test/resources/input/mp4", "000.mp4");
        builder.addInput("./src/test/resources/input/mp4/000.mp4");
        vc.setVideoSize(250, 200);
        builder.addOutput("./src/test/resources/input/mp4/000-1.mp4");
        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(1L, TimeUnit.MINUTES);
    }
}