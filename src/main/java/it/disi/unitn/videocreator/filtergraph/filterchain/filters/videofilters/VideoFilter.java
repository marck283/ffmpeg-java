package it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters;

import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;
import org.jetbrains.annotations.NotNull;

public abstract class VideoFilter extends Filter {
    protected VideoFilter(@NotNull String filterName) {
        super(filterName);
    }
}
