package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.sinks.abuffersink;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;

/**
 * This class implements FFmpeg's "abuffersink" filter.
 */
public class ABufferSink extends AudioFilter {
    /**
     * This class's constructor.
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    public ABufferSink() throws InvalidArgumentException {
        super("abuffersink");
    }

    @Override
    public void updateMap() {

    }
}
