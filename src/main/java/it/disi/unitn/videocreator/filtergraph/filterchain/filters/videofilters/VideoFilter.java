package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters;

import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used as a dummy class to identify video filters from audio ones.
 */
public abstract class VideoFilter extends Filter {

    /**
     * This class's constructor. Constructs a new video filter.
     * @param filterName The given filter's name
     */
    protected VideoFilter(@NotNull String filterName) {
        super(filterName);
    }
}
