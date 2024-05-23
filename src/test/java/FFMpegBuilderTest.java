import it.disi.unitn.FFMpeg;
import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.NotEnoughArgumentsException;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import it.disi.unitn.videocreator.VideoCreator;
import it.disi.unitn.videocreator.filtergraph.VideoFilterGraph;
import it.disi.unitn.videocreator.filtergraph.filterchain.VideoSimpleFilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.format.Format;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.Scale;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.scalingalgs.Bicubic;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

class FFMpegBuilderTest {

    @Test
    void setVideoFrameSize() throws NotEnoughArgumentsException, IOException, InvalidArgumentException, UnsupportedOperatingSystemException {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");
        VideoCreator vc = builder.newVideoCreator("./src/test/resources/input/mp4/000-1.mp4");
        vc.addInput("./src/test/resources/input/mp4/000.mp4");
        //vc.setVideoSize(250, 200, "yuvj420p", true);

        vc.setCodecID("mjpeg", true);
        vc.setPixelFormat("yuvj420p");

        Scale scale = new Scale();
        vc.setScaleParams(true, scale, new Bicubic(0.3333, 0.3333), String.valueOf(800), String.valueOf(600),
                "auto", "bt709", "auto", "auto", "init",
                "0", "disable", 0);
        VideoFilterGraph vsfg = new VideoFilterGraph();
        VideoSimpleFilterChain vsfc = new VideoSimpleFilterChain();
        Format format = vc.setFormat(new Format());
        vsfc.addAllFilters(scale, format);
        vsfg.addFilterChain(vsfc);
        vc.setVideoSimpleFilterGraph(vsfg);
        vc.createCommand();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("WIDTH: " + screenSize.getWidth());
        System.out.println("HEIGHT: " + screenSize.getHeight());

        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(1L, TimeUnit.MINUTES);
    }
}