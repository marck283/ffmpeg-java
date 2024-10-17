package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.vsplit;

import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;

/**
 * This class implements the "split" video filter.
 */
public class Split extends Filter {
    /**
     * This class's constructor. Constructs a new filter (whether video or audio).
     *
     */
    public Split() {
        super("split");
    }

    @Override
    public void updateMap() {

    }
}
