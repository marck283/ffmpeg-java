package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.sinks.nullsink;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.VideoFilter;

/**
 * This class implements the "nullsink" video filter.
 */
public class NullSink extends VideoFilter {
    /**
     * This class's constructor. Constructs a new video filter.
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    public NullSink() throws InvalidArgumentException {
        super("nullsink");
    }

    @Override
    public void updateMap() {

    }
}
