package it.disi.unitn.videocreator.transcoder;

import it.disi.unitn.FFMpeg;
import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.exceptions.UnsupportedOperatingSystemException;
import it.disi.unitn.exceptions.MultiLanguageUnsupportedOperationException;
import it.disi.unitn.videocreator.filtergraph.AudioFilterGraph;
import it.disi.unitn.videocreator.filtergraph.VideoFilterGraph;
import it.disi.unitn.videocreator.filtergraph.filterchain.AudioSimpleFilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.VideoSimpleFilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.acompressor.ACompressor;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.format.Format;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.scale.Scale;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

class VideoTranscoderTest {

    @Test
    void createCommand() throws IOException, InvalidArgumentException, UnsupportedOperatingSystemException, MultiLanguageUnsupportedOperationException {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");
        VideoTranscoder transcoder = builder.newVideoTranscoder("./src/test/resources/input/mp4/example.mov");
        transcoder.addInput("./src/test/resources/input/mp4/000.mp4");
        transcoder.enableVideoExtraction();

        //transcoder.setVideoSize(800, 600, "yuv420p", true);
        transcoder.setCodecID("mjpeg", true);
        transcoder.setPixelFormat("yuv420p");
        //transcoder.setOutFullRange(true); //If using mjpeg and YUV pixel formats, we have to set the color range to full.
        transcoder.setVideoQuality(18);

        Scale scale = new Scale();
        transcoder.setScaleParams(false, scale, null, "2048", "1920", "auto",
                "bt709", "auto", "auto", "init", "0",
                "disable", 0);
        VideoFilterGraph vsfg = new VideoFilterGraph();
        VideoSimpleFilterChain vsfc = new VideoSimpleFilterChain();
        Format format = transcoder.setFormat(new Format());
        vsfc.addAllFilters(scale, format);
        vsfg.addFilterChain(vsfc);
        transcoder.setVideoSimpleFilterGraph(vsfg);

        transcoder.createCommand(/*acomp, null, "auto", "bt709", "auto",
                "auto", "init", "0", "disable", 0*/);
        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(30L, TimeUnit.MINUTES, "./", null);
    }

    @Test
    void createCommandForAudio() throws InvalidArgumentException, IOException, MultiLanguageUnsupportedOperationException {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");
        VideoTranscoder transcoder = builder.newVideoTranscoder("./src/test/resources/input/mp4/002.wav");
        transcoder.addInput("./src/test/resources/input/mp4/002.wmv");
        transcoder.enableAudioExtraction();

        //FLAC supports encoding with 4-32 bits per sample, but encoders only support encoding with 4-24 bits per sample.
        transcoder.setAudioCodec("flac");

        ACompressor acomp = new ACompressor();
        acomp.setThreshold(0.123);
        acomp.setAttack(0.01);

        AudioFilterGraph asfg = new AudioFilterGraph();
        AudioSimpleFilterChain asfc = new AudioSimpleFilterChain();
        asfc.addFilter(acomp);
        asfg.addFilterChain(asfc);
        transcoder.setAudioSimpleFilterGraph(asfg);

        transcoder.createCommand(/*acomp, null, "auto", "bt601", "auto",
                "auto", "init", "0", "disable", 0*/);
        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(30L, TimeUnit.MINUTES, "./", null);
    }
}