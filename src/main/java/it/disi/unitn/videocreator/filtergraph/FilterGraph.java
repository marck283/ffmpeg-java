package it.disi.unitn.videocreator.filtergraph;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.FilterChain;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements a filter graph.
 */
public class FilterGraph {

    private final List<FilterChain> fcList;

    /**
     * This class's constructor.
     */
    public FilterGraph() {
        fcList = new ArrayList<>();
    }

    /**
     * Checks if the given FilterChain instance is null and throws InvalidArgumentException if it is.
     * @param filterChain The given FilterChain instance
     * @throws InvalidArgumentException If the given FilterChain instance is null
     */
    private void checkNull(FilterChain filterChain) throws InvalidArgumentException {
        if(filterChain == null) {
            throw new InvalidArgumentException("The given filter chain cannot be null.", "La filter chain fornita non " +
                    "puo' essere null.");
        }
    }

    /**
     * Adds the given filter chain to this filter graph.
     * @param filterChain The given filter chain
     * @throws InvalidArgumentException If the given filter chain is null
     */
    public void addFilterChain(@NotNull FilterChain filterChain) throws InvalidArgumentException {
        checkNull(filterChain);
        fcList.add(filterChain);
    }

    /**
     * Removes the given filter chain from this filter graph.
     * @param filterChain The given filter chain
     * @throws InvalidArgumentException If the given filter chain is null
     */
    public void removeFilterChain(@NotNull FilterChain filterChain) throws InvalidArgumentException {
        checkNull(filterChain);
        fcList.remove(filterChain);
    }

    @Override
    public String toString() {
        List<String> helperList = new ArrayList<>();
        for(FilterChain fc: fcList) {
            helperList.add(fc.toString());
        }
        return "-filter_graph \"" + String.join(";", helperList) + "\"";
    }
}
