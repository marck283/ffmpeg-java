package it.disi.unitn.videocreator.filtergraph;

import it.disi.unitn.videocreator.filtergraph.filterchain.SimpleFilterChain;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a simple filter graph.
 */

//NOTA: e' veramente necessario permettere anche alle filter chain per l'audio di entrare a far parte del simple filter
//graph dei video? Le filter chain audio, infatti, non dovrebbero usare solo i filtri AudioFilter, mentre quelle video
//solo i filtri VideoFilter?
public abstract class SimpleFilterGraph {

    protected final List<SimpleFilterChain> sfcList;


    public SimpleFilterGraph() {
        sfcList = new ArrayList<>();
    }

    /**
     * This method adds the given filter chain to the calling instance of this class.
     * @param filterChain The given filter chain
     */
    public void addFilterChain(@NotNull SimpleFilterChain filterChain) {
        sfcList.add(filterChain);
    }

    /**
     * This method removes the given filter chain from the calling instance of this class.
     * @param filterChain The given filter chain
     */
    public void removeFilterChain(@NotNull SimpleFilterChain filterChain) {
        sfcList.remove(filterChain);
    }

    /**
     * This method returns the String representation of the calling object.
     * @return The String representation of the calling object
     */
    @Override
    public abstract String toString();
}
