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
 * This class implements a new simple filter chain for the audio filters.
 */
public class AudioSimpleFilterChain extends SimpleFilterChain {

    /**
     * This class's constructor.
     */
    public AudioSimpleFilterChain() {
        super();
    }

    /**
     * Adds a new filter to this filter chain.
     * @param filter The Filter to be added. This parameter must be an instance of AudioFilter
     * @throws InvalidArgumentException If the given Filter instance is null, or it is an instance of VideoFilter
     */
    @Override
    public void addFilter(@NotNull Filter filter) throws InvalidArgumentException {
        checkNullFilter(filter);
        if(!(filter instanceof AudioFilter)) {
            throw new InvalidArgumentException("Cannot add a video filter to an audio filter chain.", "Non e' possibile " +
                    "aggiungere un filtro video ad una catena di filtri audio.");
        }
        filterList.add(filter);
    }

    /**
     * Adds all given filters if none of them is null or not an instance of AudioFilter.
     * @param filters The given Filter instances. These must be instances of AudioFilter
     * @throws InvalidArgumentException If any of the given filters is null or is not an instance of AudioFilter
     */
    public void addAllFilters(@NotNull Filter @NotNull ... filters) throws InvalidArgumentException {
        Stream<Filter> filterStr = Arrays.stream(filters), filterStr1 = Arrays.stream(filters);
        if(filterStr.anyMatch(Objects::isNull)) {
            throw new InvalidArgumentException("Cannot add null filters.", "Non e' possibile aggiungere filtr null.");
        }
        if(filterStr1.anyMatch(f -> !(f instanceof AudioFilter))) {
            throw new InvalidArgumentException("All given filters must be instances of AudioFilter.", "Tutti i filtri " +
                    "forniti devono essere istanze di AudioFilter.");
        }
        filterList.addAll(Arrays.asList(filters));
    }

    /**
     * Removes the given filter from this filter chain.
     * @param filter The Filter to be removed. This parameter must be an instance of AudioFilter
     * @throws InvalidArgumentException If the given Filter instance is null, or it is an instance of VideoFilter
     */
    @Override
    public void removeFilter(@NotNull Filter filter) throws InvalidArgumentException {
        checkNullFilter(filter);
        if(!(filter instanceof VideoFilter)) {
            throw new InvalidArgumentException("Cannot remove a video filter from an audio filter chain.", "Non e' possibile " +
                    "rimuovere un filtro video da una catena di filtri audio.");
        }
        filterList.remove(filter);
    }
}
