package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.sinks.nullsink;

import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.VideoFilter;

/**
 * This class implements the "nullsink" video filter.
 */
public class NullSink extends VideoFilter {
    /**
     * This class's constructor. Constructs a new video filter.
     *
     */
    public NullSink() {
        super("nullsink");
    }

    @Override
    public void updateMap() {

    }
}
