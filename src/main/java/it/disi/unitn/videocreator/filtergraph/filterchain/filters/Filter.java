package it.disi.unitn.videocreator.filtergraph.filterchain.filters;

import it.disi.unitn.StringExt;
import it.disi.unitn.exceptions.InvalidArgumentException;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * This class is the abstract representation of a filter to be added to a filter chain.
 */
public abstract class Filter {

    private final String filterName;

    /**
     * The set of options to be used in the filter.
     */
    protected final Map<String, String> options;

    private final List<String> input, output;

    /**
     * The system's Locale.
     */
    protected final Locale l;

    /**
     * This class's constructor. Constructs a new filter (whether video or audio).
     * @param filterName The given filter's name.
     */
    protected Filter(@NotNull String filterName) {
        l = Locale.getDefault();

        if(StringExt.checkNullOrEmpty(filterName)) {
            System.err.println((new InvalidArgumentException("The filter's name cannot be null or an empty string.",
                    "Il nome del filtro non puo' essere null o una stringa vuota.").getMessage()));
        }
        this.filterName = filterName;
        options = new LinkedHashMap<>();
        input = new ArrayList<>();
        output = new ArrayList<>();
    }

    /**
     * Private method to check if the argument is null or an empty string.
     * @param streamName The given argument
     * @throws InvalidArgumentException If the given argument is null or an empty string.
     */
    private void checkNullStreamName(@NotNull String streamName) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(streamName)) {
            throw new InvalidArgumentException("The inputs and the outputs to the filters cannot be null or empty strings.",
                    "Gli input e gli output dei filtri non possono essere null o stringhe vuote.");
        }
    }

    /**
     * Adds an input stream to this filter chain. The given input stream must be expressed without the square brackets
     * around.
     * @param streamName The input stream's name
     * @throws InvalidArgumentException If the given argument is null or an empty string
     */
    public void addInput(@NotNull String streamName) throws InvalidArgumentException {
        checkNullStreamName(streamName);
        if(!streamName.isEmpty()) {
            input.add("[" + streamName + "]");
        }
    }

    /**
     * Adds an output stream to this filter chain. The given output stream must be expressed without the square brackets
     * around.
     * @param streamName The output stream's name
     * @throws InvalidArgumentException If the given argument is null or an empty string
     */
    public void addOutput(@NotNull String streamName) throws InvalidArgumentException {
        checkNullStreamName(streamName);
        if(!streamName.isEmpty()) {
            output.add("[" + streamName + "]");
        }
    }

    /**
     * Updates the options for the calling Filter instance.
     */
    public abstract void updateMap();

    @Override
    public String toString() {
        List<String> helpList = new ArrayList<>();
        for(Map.Entry<String, String> e: options.entrySet()) {
            if(e.getValue() != null && !e.getValue().isEmpty()) {
                helpList.add(e.getKey() + "=" + e.getValue());
            }
        }

        String inputString = String.join("", input), outputString = String.join("", output);
        return inputString + String.join("=", filterName, String.join(":", helpList)).trim().
                replaceAll("  +", " ") + outputString;
    }

    /**
     * This method allows the program to set the filter's options if the given condition is met.
     * @param cond The given condition.
     * @param oname The given option's name. This value cannot be null or an empty string.
     * @param val The given option's value. This value cannot be null or an empty string.
     * @throws InvalidArgumentException If either the option's name or its value (or both) is null or an empty string
     */
    protected void setOptionWithCond(boolean cond, @NotNull String oname, @NotNull String val) throws InvalidArgumentException {
        if(cond) {
            setOption(oname, val);
        }
    }

    /**
     * This method allows the program to set the filter's options even without any condition.
     * @param oname The given option's name. This value cannot be null or an empty string.
     * @param val The given option's value. This value cannot be null or an empty string.
     * @throws InvalidArgumentException If either the option's name or its value (or both) is null or an empty string
     */
    protected void setOption(@NotNull String oname, @NotNull String val) throws InvalidArgumentException {
        if(StringExt.checkNullOrEmpty(oname)) {
            throw new InvalidArgumentException("The option's name cannot be null or an empty string.", "Il nome " +
                    "dell'opzione non puo' essere null o una stringa vuota.");
        }

        if(!StringExt.checkNullOrEmpty(val)) {
            options.put(oname, val);
        }
    }
}
