package it.disi.unitn.videocreator.filtergraph;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.FilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.SimpleFilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.filters.Filter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a simple filter graph.
 */

//NOTA: e' veramente necessario permettere anche alle filter chain per l'audio di entrare a far parte del simple filter
//graph dei video? Le filter chain audio, infatti, non dovrebbero usare solo i filtri AudioFilter, mentre quelle video
//solo i filtri VideoFilter?
public abstract class SimpleFilterGraph extends FilterGraph {

    /**
     * The list of SimpleFilterChain instances to be used in this filter graph.
     */
    protected final List<SimpleFilterChain> sfcList;

    /**
     * This class's constructor.
     */
    public SimpleFilterGraph() {
        sfcList = new ArrayList<>();
    }

    /**
     * This method adds the given filter chain to the calling instance of this class.
     * @param filterChain The given filter chain
     * @throws InvalidArgumentException If the given filter chain is not an instance of SimpleFilterChain
     */
    @Override
    public void addFilterChain(@NotNull FilterChain filterChain) throws InvalidArgumentException {
        if(!(filterChain instanceof SimpleFilterChain)) {
            throw new InvalidArgumentException("The given filter chain must be an instance of SimpleFitlerChain.", "La " +
                    "catena di filtri fornita in input deve essere un'istanza di SimpleFilterChain.");
        }
        sfcList.add((SimpleFilterChain) filterChain);
    }

    /**
     * This method removes the given filter chain from the calling instance of this class.
     * @param filterChain The given filter chain
     * @throws InvalidArgumentException If the given filter chain is not an instance of SimpleFilterChain
     */
    @Override
    public void removeFilterChain(@NotNull FilterChain filterChain) throws InvalidArgumentException {
        if(!(filterChain instanceof SimpleFilterChain)) {
            throw new InvalidArgumentException("The given filter chain must be an instance of SimpleFitlerChain.", "La " +
                    "catena di filtri fornita in input deve essere un'istanza di SimpleFilterChain.");
        }
        sfcList.remove(filterChain);
    }

    /**
     * This method returns the String representation of the calling object.
     * @return The String representation of the calling object
     */
    @Override
    public abstract String toString();
}
