package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.asplit;

import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;

/**
 * This class implements the "asplit" audio filter.
 */
public class ASplit extends AudioFilter {
    /**
     * This class's constructor. Constructs a new filter (whether video or audio).
     *
     */
    public ASplit() {
        super("asplit");
    }

    @Override
    public void updateMap() {

    }
}
