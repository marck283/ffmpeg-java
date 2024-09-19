package it.disi.unitn.filters.audio;

import it.disi.unitn.FFMpegBuilder;
import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.AudioFilterGraph;
import it.disi.unitn.videocreator.filtergraph.filterchain.AudioSimpleFilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.abuffer.ABuffer;
import org.junit.jupiter.api.Test;

public class ABufferTest {

    @Test
    public void test() throws InvalidArgumentException {
        FFMpegBuilder builder = new FFMpegBuilder("ffmpeg");

        ABuffer filter = new ABuffer();
        filter.setTimeBase(1, 2);

        AudioFilterGraph afg = new AudioFilterGraph();
        AudioSimpleFilterChain afc = new AudioSimpleFilterChain();
        afc.addFilter(filter);
        afg.addFilterChain(afc);

        //builder.
    }

}
