package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.sinks.buffersink;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.VideoFilter;

/**
 * This class implements the "buffersink" video filter.
 */
public class BufferSink extends VideoFilter {
    /**
     * This class's constructor. Constructs a new video filter.
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    public BufferSink() throws InvalidArgumentException {
        super("buffersink");
    }

    @Override
    public void updateMap() {

    }
}
