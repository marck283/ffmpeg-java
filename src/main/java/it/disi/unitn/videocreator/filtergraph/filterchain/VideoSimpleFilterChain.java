package it.disi.unitn.videocreator.filtergraph.filterchain;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.VideoFilter;
import org.jetbrains.annotations.NotNull;

public class VideoSimpleFilterChain extends SimpleFilterChain {

    public void addFilter(@NotNull Filter filter) throws InvalidArgumentException {
        if(!(filter instanceof VideoFilter)) {
            System.err.println("Cannot add an audio filter to a video filter chain.");
            System.exit(1);
        }
        checkNullFilter(filter);
        filterList.add(filter);
    }

    public void removeFilter(@NotNull Filter filter) throws InvalidArgumentException {
        if(!(filter instanceof VideoFilter)) {
            System.err.println("Cannot remove an audio filter from a video filter chain.");
            System.exit(1);
        }
        checkNullFilter(filter);
        filterList.remove(filter);
    }

}
