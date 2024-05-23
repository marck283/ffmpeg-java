package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.vsplit;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;

/**
 * This class implements the "split" video filter.
 */
public class Split extends Filter {
    /**
     * This class's constructor. Constructs a new filter (whether video or audio).
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    public Split() throws InvalidArgumentException {
        super("split");
    }

    @Override
    public void updateMap() {

    }
}
