package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;
import org.jetbrains.annotations.NotNull;

/**
 * This is just a dummy class to identify audio filters from video ones.
 */
public abstract class AudioFilter extends Filter {

    /**
     * This class's constructor.
     * @param filterName The filter's name
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    protected AudioFilter(@NotNull String filterName) throws InvalidArgumentException {
        super(filterName);
    }
}
