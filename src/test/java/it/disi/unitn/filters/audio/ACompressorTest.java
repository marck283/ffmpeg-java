package it.disi.unitn.filters.audio;

import it.disi.unitn.FFMpeg;
import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.lasagna.audiocreator.AudioFiltering;
import it.disi.unitn.videocreator.filtergraph.AudioFilterGraph;
import it.disi.unitn.videocreator.filtergraph.filterchain.AudioSimpleFilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.acompressor.ACompressor;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ACompressorTest {

    @Test
    public void test() throws InvalidArgumentException, IOException {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");

        AudioFiltering audioFiltering = builder.newAudioFiltering("./src/test/resources/output/audio/acompressor.mp3");
        audioFiltering.addInput("./src/test/resources/input/audio/input.mp3");

        AudioFilterGraph afg = new AudioFilterGraph();
        AudioSimpleFilterChain afc = new AudioSimpleFilterChain();

        ACompressor acompr = new ACompressor();
        acompr.setThreshold(-21);
        acompr.setRatio(9);
        acompr.setAttack(200);
        acompr.setRelease(1000);
        acompr.updateMap();

        afc.addFilter(acompr);
        afg.addFilterChain(afc);
        audioFiltering.setAudioFilterGraph(afg);
        audioFiltering.createCommand();

        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(10L, TimeUnit.SECONDS, "./", null);
    }

}
