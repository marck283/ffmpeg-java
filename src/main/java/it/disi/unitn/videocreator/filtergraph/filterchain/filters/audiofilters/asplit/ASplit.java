package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.asplit;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;

/**
 * This class implements the "asplit" audio filter.
 */
public class ASplit extends Filter {
    /**
     * This class's constructor. Constructs a new filter (whether video or audio).
     *
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    public ASplit() throws InvalidArgumentException {
        super("asplit");
    }

    @Override
    public void updateMap() {

    }
}
