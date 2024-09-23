package it.disi.unitn.filters.audio;

import it.disi.unitn.FFMpeg;
import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.lasagna.audiocreator.AudioFiltering;
import it.disi.unitn.videocreator.filtergraph.AudioFilterGraph;
import it.disi.unitn.videocreator.filtergraph.filterchain.AudioSimpleFilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.acontrast.AContrast;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class AContrastTest {

    @Test
    public void test() throws InvalidArgumentException, IOException {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");

        AudioFilterGraph afg = new AudioFilterGraph();
        AudioSimpleFilterChain afc = new AudioSimpleFilterChain();

        AContrast acontrast = new AContrast();
        acontrast.setContrast(100);
        acontrast.updateMap();

        afc.addFilter(acontrast);
        afg.addFilterChain(afc);

        AudioFiltering audioFiltering = builder.newAudioFiltering("./src/test/resources/output/audio/acontrast.mp3");
        audioFiltering.addInput("./src/test/resources/input/audio/input.mp3");
        audioFiltering.setAudioFilterGraph(afg);
        audioFiltering.createCommand();

        FFMpeg ffmpeg = builder.build();
        ffmpeg.executeCMD(10L, TimeUnit.SECONDS, "./", null);
    }

}
