package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.sinks.abuffersink;

import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;

/**
 * This class implements FFmpeg's "abuffersink" filter.
 */
public class ABufferSink extends AudioFilter {
    /**
     * This class's constructor.
     *
     */
    public ABufferSink() {
        super("abuffersink");
    }

    @Override
    public void updateMap() {

    }
}
