package it.disi.unitn.videocreator;

import it.disi.unitn.FFMpeg;
import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.NotEnoughArgumentsException;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import it.disi.unitn.videocreator.filtergraph.VideoSimpleFilterGraph;
import it.disi.unitn.videocreator.filtergraph.filterchain.VideoSimpleFilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.format.Format;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.Scale;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.scalingalgs.Bicubic;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

class VideoCreatorTest {

    @Test
    void createCommand() throws NotEnoughArgumentsException, InvalidArgumentException, UnsupportedOperatingSystemException,
            IOException {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");
        VideoCreator creator = builder.newVideoCreator("./src/test/resources/input/mp4/example.mp4");
        creator.addInput("./src/test/resources/input/images/000.jpeg");
        //creator.setVideoSize(800, 600, "yuv420p", true);
        //creator.setCodecID("mjpeg", true); //No need to set the codec ID if we maintain the picture format
        creator.setPixelFormat("yuv420p");
        creator.setVideoStreamCopy(true);
        //creator.setOutFullRange(true); //If using mjpeg and YUV pixel formats, we have to set the color range to full.
        creator.setVideoQuality(18);

        Scale scale = new Scale();
        creator.setScaleParams(true, scale, new Bicubic(0.3333, 0.3333), String.valueOf(800), String.valueOf(600),
                "auto", "bt709", "auto", "auto", "init",
                "0", "disable", 0);

        VideoSimpleFilterGraph vsfg = new VideoSimpleFilterGraph();
        VideoSimpleFilterChain vsfc = new VideoSimpleFilterChain();
        Format format = creator.setFormat(new Format());
        vsfc.addAllFilters(scale, format);
        vsfg.addFilterChain(vsfc);
        creator.setVideoSimpleFilterGraph(vsfg);

        creator.createCommand(/*true, null, new Bicubic(0.3333, 0.3333), "auto",
                "bt709", "auto", "auto", "init", "0",
                "disable", 0*/);

        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(30L, TimeUnit.SECONDS);
    }
}