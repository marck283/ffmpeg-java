package it.disi.unitn.videocreator.filtergraph.filterchain;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.audiofilters.AudioFilter;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.VideoFilter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * This class is used to distinguish video simple filter chains from audio ones.
 */
public class VideoSimpleFilterChain extends SimpleFilterChain {

    /**
     * This class's constructor.
     */
    public VideoSimpleFilterChain() {
        super();
    }

    public void addFilter(@NotNull Filter filter) throws InvalidArgumentException {
        if(!(filter instanceof VideoFilter)) {
            System.err.println("Cannot add an audio filter to a video filter chain.");
            System.exit(1);
        }
        checkNullFilter(filter);
        filterList.add(filter);
    }

    /**
     * Adds all given filters if none of them is null or not an instance of VideoFilter.
     * @param filters The given Filter instances. These must be instances of VideoFilter
     * @throws InvalidArgumentException If any of the given filters is null or is not an instance of VideoFilter
     */
    public void addAllFilters(@NotNull Filter @NotNull ... filters) throws InvalidArgumentException {
        Stream<Filter> filterStr = Arrays.stream(filters), filterStr1 = Arrays.stream(filters);
        if(filterStr.anyMatch(Objects::isNull)) {
            throw new InvalidArgumentException("Cannot add null filters.", "Non e' possibile aggiungere filtr null.");
        }
        if(filterStr1.anyMatch(f -> !(f instanceof VideoFilter))) {
            throw new InvalidArgumentException("All given filters must be instances of AudioFilter.", "Tutti i filtri " +
                    "forniti devono essere istanze di AudioFilter.");
        }
        filterList.addAll(Arrays.asList(filters));
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
