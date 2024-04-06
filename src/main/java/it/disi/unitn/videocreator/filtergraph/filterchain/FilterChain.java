package it.disi.unitn.videocreator.filtergraph.filterchain;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the implementation of the filter chain.
 */
public class FilterChain {

    /**
     * The list of filters to be used in this filter chain.
     */
    protected final List<Filter> filterList;

    private final List<String> input, output;

    /**
     * This class's constructor.
     */
    public FilterChain() {
        filterList = new ArrayList<>();
        input = new ArrayList<>();
        output = new ArrayList<>();
    }

    /**
     * Private method to check if the argument is null or an empty string.
     * @param streamName The given argument
     * @throws InvalidArgumentException If the given argument is null or an empty string.
     */
    private void checkNullOrEmpty(@NotNull String streamName) throws InvalidArgumentException {
        if(streamName == null || streamName.isEmpty()) {
            throw new InvalidArgumentException("The inputs and the outputs to the filters cannot be null or empty strings.",
                    "Gli input e gli output dei filtri non possono essere null o stringhe vuote.");
        }
    }

    /**
     * Private method to check if the given Filter instance is null.
     * @param filter The given Filter instance
     * @throws InvalidArgumentException If the given Filter instance is null
     */
    private void checkNullFilter(@NotNull Filter filter) throws InvalidArgumentException {
        if(filter == null) {
            throw new InvalidArgumentException("No new Filter instance can be null.", "Nessuna nuova istanza di Filter puo' " +
                    "essere null.");
        }
    }

    /**
     * Adds an input stream to this filter chain. The given input stream must be expressed without the square brackets
     * around.
     * @param streamName The input stream's name
     * @throws InvalidArgumentException If the given argument is null or an empty string
     */
    public void addInput(@NotNull String streamName) throws InvalidArgumentException {
        checkNullOrEmpty(streamName);
        input.add("[" + streamName + "]");
    }

    /**
     * Adds an output stream to this filter chain. The given output stream must be expressed without the square brackets
     * around.
     * @param streamName The output stream's name
     * @throws InvalidArgumentException If the given argument is null or an empty string
     */
    public void addOutput(@NotNull String streamName) throws InvalidArgumentException {
        checkNullOrEmpty(streamName);
        output.add("[" + streamName + "]");
    }

    /**
     * Adds a filter to this filter chain.
     * @param filter The Filter to be added
     * @throws InvalidArgumentException If the given argument is null
     */
    public void addFilter(@NotNull Filter filter) throws InvalidArgumentException {
        checkNullFilter(filter);
        filterList.add(filter);
    }

    /**
     * Removes the given Filter from this filter chain
     * @param filter The Filter to be removed
     * @throws InvalidArgumentException If the given argument is null
     */
    public void removeFilter(@NotNull Filter filter) throws InvalidArgumentException {
        checkNullFilter(filter);
        filterList.remove(filter);
    }

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

        String inputString = String.join("", input), outputString = String.join("", output);
        return inputString + String.join(",", helpList).trim().replaceAll("  +", " ") +
                outputString;
    }
}
