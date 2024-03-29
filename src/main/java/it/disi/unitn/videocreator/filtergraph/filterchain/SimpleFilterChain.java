package it.disi.unitn.videocreator.filtergraph.filterchain;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class SimpleFilterChain {

    protected final List<Filter> filterList;

    public SimpleFilterChain() {
        filterList = new ArrayList<>();
    }

    /**
     * Protected method to check if the given Filter instance is null.
     * @param filter The given Filter instance
     * @throws InvalidArgumentException If the given Filter instance is null
     */
    protected void checkNullFilter(@NotNull Filter filter) throws InvalidArgumentException {
        if(filter == null) {
            throw new InvalidArgumentException("No new Filter instance can be null.", "Nessuna nuova istanza di Filter puo' " +
                    "essere null.");
        }
    }

    /**
     * Adds a filter to this filter chain.
     * @param filter The Filter to be added
     * @throws InvalidArgumentException If the given argument is null
     */
    public abstract void addFilter(@NotNull Filter filter) throws InvalidArgumentException;

    /**
     * Removes the given Filter from this filter chain
     * @param filter The Filter to be removed
     * @throws InvalidArgumentException If the given argument is null
     */
    public abstract void removeFilter(@NotNull Filter filter) throws InvalidArgumentException;

    /**
     * This method returns the String representation of this filter chain.
     * @return The String representation of this filter chain
     */
    @Override
    public String toString() {
        List<String> helpList = new ArrayList<>();
        for(Filter f: filterList) {
            helpList.add(f.toString());
        }

        return String.join(",", helpList).trim().replaceAll("  +", " ");
    }

}
