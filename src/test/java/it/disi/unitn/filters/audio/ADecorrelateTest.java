package it.disi.unitn.filters.audio;

import it.disi.unitn.FFMpeg;
import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.lasagna.audiocreator.AudioFiltering;
import it.disi.unitn.videocreator.filtergraph.AudioFilterGraph;
import it.disi.unitn.videocreator.filtergraph.filterchain.AudioSimpleFilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.adecorrelate.ADecorrelate;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ADecorrelateTest {

    @Test
    public void test() throws InvalidArgumentException, IOException {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");

        AudioFilterGraph afg = new AudioFilterGraph();
        AudioSimpleFilterChain afc = new AudioSimpleFilterChain();

        ADecorrelate adecorrelate = new ADecorrelate();
        adecorrelate.setStages(2);
        adecorrelate.setSeed(2);
        adecorrelate.updateMap();

        afc.addFilter(adecorrelate);
        afg.addFilterChain(afc);

        AudioFiltering audioFiltering = builder.newAudioFiltering("./src/test/resources/output/audio/adecorrelate.mp3");
        audioFiltering.addInput("./src/test/resources/input/audio/input.mp3");
        audioFiltering.setAudioFilterGraph(afg);
        audioFiltering.createCommand();

        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(10L, TimeUnit.SECONDS, "./", null);
    }

}
