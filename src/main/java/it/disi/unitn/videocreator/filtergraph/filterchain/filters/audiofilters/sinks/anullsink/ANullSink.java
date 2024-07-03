package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.sinks.anullsink;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;

/**
 * This class implements the "anullsink" audio filter.
 */
public class ANullSink extends AudioFilter {
    /**
     * This class's constructor.
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    public ANullSink() throws InvalidArgumentException {
        super("anullsink");
    }

    @Override
    public void updateMap() {

    }
}
