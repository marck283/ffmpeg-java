package it.disi.unitn.videocreator;

import it.disi.unitn.FFMpeg;
import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import it.disi.unitn.exceptions.MultiLanguageUnsupportedOperationException;
import it.disi.unitn.videocreator.filtergraph.FilterGraph;
import it.disi.unitn.videocreator.filtergraph.filterchain.FilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.abuffer.ABuffer;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.adecorrelate.ADecorrelate;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.aformat.AFormat;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.aformat.ChannelLayout;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.format.Format;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.Scale;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.scalingalgs.Bicubic;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

class VideoCreatorTest {

    @Test
    void createCommand() throws InvalidArgumentException, UnsupportedOperatingSystemException,
            IOException, MultiLanguageUnsupportedOperationException {
        FFMpegBuilder builder = new FFMpegBuilder();
        builder.add("-xerror");
        VideoCreator creator = builder.newVideoCreator("./src/test/resources/output/mp4/001.wmv");
        creator.addInput("./src/test/resources/input/mp4/000.wmv");
        //creator.addInput("./src/test/resources/input/mp3/000.mp3");
        //creator.setVideoSize(800, 600, "yuv420p", true);
        //creator.setCodecID("mjpeg", true); //No need to set the codec ID if we maintain the picture format
        creator.setPixelFormat("yuv420p");
        //creator.setVideoStreamCopy(true);
        //creator.setOutFullRange(true); //If using mjpeg and YUV pixel formats, we have to set the color range to full.
        creator.setVideoQuality(18);

        Scale scale = new Scale();
        creator.setScaleParams(false, scale, new Bicubic(0.3333, 0.3333), String.valueOf(800), String.valueOf(600),
                "auto", "bt709", "auto", "auto", "init",
                "0", "disable", 0);
        scale.addInput("0:v");

        Format format = creator.setFormat(new Format());
        //format.addOutput("vout");

        ABuffer abuffer = new ABuffer();

        ChannelLayout chlay = new ChannelLayout();
        chlay.addChannelID("stereo");
        //chlay.addChannelID("quad");
        abuffer.setSampleFmt("u8");
        abuffer.setSampleRate(400);
        abuffer.setChannelLayout(chlay);
        abuffer.setChannels(2);
        abuffer.updateMap();

        AFormat aformat = new AFormat();
        //aformat.addOutput("ain");
        aformat.addSampleFormat("u8");
        aformat.addSampleFormat("s16p");

        chlay.addChannelID("quad");
        aformat.addChannelLayout(chlay);
        aformat.updateMap();

        ADecorrelate adecor = new ADecorrelate();
        //adecor.addInput("ain");
        adecor.setSeed(100);
        //adecor.addOutput("a");
        adecor.updateMap();

        FilterGraph vsfg = new FilterGraph();
        FilterChain vsfc = new FilterChain(), vsfca = new FilterChain();
        vsfc.addAllFilters(scale, format);
        vsfca.addAllFilters(abuffer, aformat, adecor);
        vsfg.addFilterChain(vsfc);
        vsfg.addFilterChain(vsfca);
        creator.setComplexFilterGraph(vsfg);

        creator.createCommand(/*true, null, new Bicubic(0.3333, 0.3333), "auto",
                "bt709", "auto", "auto", "init", "0",
                "disable", 0*/);

        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(30L, TimeUnit.SECONDS, "./", null);
    }

    @Test
    void createCommandTest1() throws InvalidArgumentException, UnsupportedOperatingSystemException, IOException, MultiLanguageUnsupportedOperationException {
        FFMpegBuilder builder = new FFMpegBuilder();
        VideoCreator creator = builder.newVideoCreator("./src/test/resources/input/mp4/002.mp4");
        creator.addInput("./src/test/resources/input/images/000.jpeg");
        creator.setPixelFormat("yuv420p");
        creator.setVideoQuality(18);

        /*Buffer buffer = new Buffer();
        buffer.setVideoWidthAndHeight(800, 600);
        buffer.setPixFmt("yuv420p");
        buffer.setTimeBase("1/24");
        buffer.setSampleAspectRatio(1);
        //buffer.addInput("0:v");
        buffer.addOutput("vin");
        buffer.updateMap();*/

        Scale scale = new Scale();
        creator.setScaleParams(false, scale, new Bicubic(2, 2), String.valueOf(800),
                String.valueOf(600),
                "auto", "bt709", "auto", "auto", "init",
                "0", "disable", 2);
        //scale.addInput("vin");
        scale.addInput("0:0");
        //scale.addOutput("vout");

        Format format = creator.setFormat(new Format());
        //format.addInput("vout");
        //format.addOutput("0:v");

        ABuffer abuffer = new ABuffer();
        //abuffer.addOutput("ain1");

        ChannelLayout chlay = new ChannelLayout();
        chlay.setChannelID("stereo");
        abuffer.setSampleFmt("u8");
        abuffer.setSampleRate(400);
        abuffer.setChannelLayout(chlay);
        abuffer.updateMap();

        AFormat aformat = new AFormat();
        //aformat.addInput("ain1");
        //aformat.addOutput("ain");
        aformat.addSampleFormat("u8");
        aformat.addSampleFormat("s16p");

        aformat.addChannelLayout(chlay);
        aformat.updateMap();

        ADecorrelate adecor = new ADecorrelate();
        //adecor.addInput("ain");
        //adecor.addOutput("aout");
        adecor.setSeed(100);
        adecor.updateMap();

        FilterGraph vfg = new FilterGraph();
        FilterChain fc = new FilterChain()/*, fc1 = new FilterChain()*/;
        fc.addAllFilters(/*buffer, */scale, format, abuffer, aformat, adecor);
        //fc1.addAllFilters(abuffer, aformat, adecor);
        vfg.addFilterChain(fc);
        //vfg.addFilterChain(fc1);
        creator.setComplexFilterGraph(vfg);

        creator.createCommand(/*true, null, new Bicubic(0.3333, 0.3333), "auto",
                "bt709", "auto", "auto", "init", "0",
                "disable", 0*/);

        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(30L, TimeUnit.SECONDS, "./", null);
    }
}