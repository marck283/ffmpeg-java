package it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters;

import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;
import org.jetbrains.annotations.NotNull;

public abstract class AudioFilter extends Filter {
    protected AudioFilter(@NotNull String filterName) {
        super(filterName);
    }
}
