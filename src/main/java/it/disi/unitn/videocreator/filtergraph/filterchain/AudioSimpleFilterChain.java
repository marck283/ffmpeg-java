package it.disi.unitn.videocreator.filtergraph.filterchain;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.VideoFilter;
import org.jetbrains.annotations.NotNull;

public class AudioSimpleFilterChain extends SimpleFilterChain {

    @Override
    public void addFilter(@NotNull Filter filter) throws InvalidArgumentException {
        if(!(filter instanceof AudioFilter)) {
            System.err.println("Cannot add a video filter to an audio filter chain.");
            System.exit(1);
        }
        checkNullFilter(filter);
        filterList.add(filter);
    }

    @Override
    public void removeFilter(@NotNull Filter filter) throws InvalidArgumentException {
        if(!(filter instanceof VideoFilter)) {
            System.err.println("Cannot remove a video filter from an audio filter chain.");
            System.exit(1);
        }
        checkNullFilter(filter);
        filterList.remove(filter);
    }
}
