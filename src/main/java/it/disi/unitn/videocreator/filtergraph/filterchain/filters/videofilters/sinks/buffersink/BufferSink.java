package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.sinks.buffersink;

import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.VideoFilter;

/**
 * This class implements the "buffersink" video filter.
 */
public class BufferSink extends VideoFilter {
    /**
     * This class's constructor. Constructs a new video filter.
     *
     */
    public BufferSink() {
        super("buffersink");
    }

    @Override
    public void updateMap() {

    }
}
