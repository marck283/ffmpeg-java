package it.disi.unitn.videocreator.filtergraph.filterchain;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.videofilters.VideoFilter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * This class is used to distinguish video simple filter chains from audio ones.
 */
public class VideoSimpleFilterChain extends FilterChain {

    /**
     * The class's constructor.
     */
    public VideoSimpleFilterChain() {
        super();
    }

    public void addFilter(@NotNull Filter filter) throws InvalidArgumentException {
        checkNullFilter(filter);
        if(!(filter instanceof VideoFilter)) {
            throw new InvalidArgumentException("Cannot add an audio filter to a video filter chain.", "Non e' possibile " +
                    "aggiungere un filtro audio ad una catena di filtri video.");
        }
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
            throw new InvalidArgumentException("Cannot add null filters.", "Non e' possibile aggiungere filtri null.");
        }
        if(filterStr1.anyMatch(f -> !(f instanceof VideoFilter))) {
            throw new InvalidArgumentException("All given filters must be instances of VideoFilter.", "Tutti i filtri " +
                    "forniti devono essere istanze di VideoFilter.");
        }
        filterList.addAll(Arrays.asList(filters));
    }

    /**
     * Removes the given Filter instance from this Video Filter Chain.
     * @param filter The Filter to be removed
     * @throws InvalidArgumentException If the given Filter instance is null or not an instance of VideoFilter
     */
    public void removeFilter(@NotNull Filter filter) throws InvalidArgumentException {
        checkNullFilter(filter);
        if(!(filter instanceof VideoFilter)) {
            throw new InvalidArgumentException("Cannot remove an audio filter from a video filter chain.", "Non e' possibile " +
                    "rimuovere un filtro audio da una catena di filtri video.");
        }
        filterList.remove(filter);
    }
}
