package it.disi.unitn.videocreator.filtergraph.filterchain.filters;

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
    protected Map<String, String> options;

    /**
     * This method chcks if the given parameter is null or an empty string.
     * @param val The given parameter
     * @return "true" if the given parameter is null or an empty string, otherwise "false".
     */
    protected static boolean checkNullOrEmpty(String val) {
        return val == null || val.isEmpty();
    }

    /**
     * This class's constructor. Constructs a new filter (whether video or audio).
     * @param filterName The given filter's name.
     * @throws InvalidArgumentException If the given filter's name is null or an empty string
     */
    protected Filter(@NotNull String filterName) throws InvalidArgumentException {
        if(filterName == null || filterName.isEmpty()) {
            throw new InvalidArgumentException("The filter's name cannot be null or an empty string.", "Il nome del " +
                    "filtro non puo' essere null o una stringa vuota.");
        }
        this.filterName = filterName;
        options = new LinkedHashMap<>();
    }

    @Override
    public String toString() {
        List<String> helpList = new ArrayList<>();
        for(Map.Entry<String, String> e: options.entrySet()) {
            if(e.getValue() != null && !e.getValue().isEmpty()) {
                helpList.add(e.getKey() + "=" + e.getValue());
            }
        }
        return String.join("=", filterName, String.join(":", helpList));
    }
}
