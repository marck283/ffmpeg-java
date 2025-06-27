package it.disi.unitn.filters.audio;

import it.disi.unitn.FFMpeg;
import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.MultiLanguageUnsupportedOperationException;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import it.disi.unitn.videocreator.VideoCreator;
import it.disi.unitn.videocreator.filtergraph.AudioFilterGraph;
import it.disi.unitn.videocreator.filtergraph.VideoFilterGraph;
import it.disi.unitn.videocreator.filtergraph.filterchain.AudioSimpleFilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.VideoSimpleFilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.abuffer.ABuffer;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.adecorrelate.ADecorrelate;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.aformat.AFormat;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.aformat.ChannelLayout;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.Scale;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.scalingalgs.Bicubic;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ABufferTest {

    @Test
    void test() throws InvalidArgumentException, UnsupportedOperatingSystemException, IOException, MultiLanguageUnsupportedOperationException {
        FFMpegBuilder builder = new FFMpegBuilder();
        VideoCreator creator = builder.newVideoCreator("./src/test/resources/input/mp4/002.mp4");
        creator.addInput("./src/test/resources/input/images/000.jpeg");
        creator.setPixelFormat("yuv420p");
        creator.setVideoQuality(18);

        AudioFilterGraph afg = new AudioFilterGraph();
        AudioSimpleFilterChain asfc = new AudioSimpleFilterChain();
        ABuffer abuffer = new ABuffer();
        abuffer.addOutput("ain1");

        ChannelLayout chlay = new ChannelLayout();
        chlay.setChannelID("stereo");
        abuffer.setChannelLayout(chlay);
        abuffer.updateMap();

        AFormat aformat = new AFormat();
        aformat.addInput("ain1");
        aformat.addOutput("ain");
        aformat.addSampleFormat("u8");
        aformat.addSampleFormat("s16");

        aformat.addChannelLayout(chlay);
        aformat.updateMap();

        ADecorrelate adecor = new ADecorrelate();
        adecor.addInput("ain");
        adecor.updateMap();
        asfc.addAllFilters(abuffer, aformat, adecor);
        afg.addFilterChain(asfc);
        creator.setAudioSimpleFilterGraph(afg);

        VideoFilterGraph vfg = new VideoFilterGraph();
        VideoSimpleFilterChain vsfc = new VideoSimpleFilterChain();
        Scale scale = new Scale();
        creator.setScaleParams(false, scale, new Bicubic(0.3333, 0.3333), String.valueOf(800), String.valueOf(600),
                "auto", "bt709", "auto", "auto", "init",
                "0", "disable", 0);
        vsfc.addFilter(scale);
        vfg.addFilterChain(vsfc);
        creator.setVideoSimpleFilterGraph(vfg);

        creator.createCommand();

        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(30L, TimeUnit.SECONDS, "./", null);
    }

}
