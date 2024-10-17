package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.sinks.anullsink;

import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;

/**
 * This class implements the "anullsink" audio filter.
 */
public class ANullSink extends AudioFilter {
    /**
     * This class's constructor.
     *
     */
    public ANullSink() {
        super("anullsink");
    }

    @Override
    public void updateMap() {

    }
}
