package it.disi.unitn.videocreator.filtergraph;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.AudioSimpleFilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.FilterChain;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to instantiate a new simple filter graph for the audio track.
 */
public class AudioFilterGraph extends FilterGraph {

    /**
     * The class's constructor.
     */
    public AudioFilterGraph() {
        super();
    }

    /**
     * This method adds the given filter chain to the calling instance of this class.
     * @param filterChain The given filter chain
     * @throws InvalidArgumentException If the given filter chain is not an instance of AudioSimpleFilterChain
     */
    @Override
    public void addFilterChain(@NotNull FilterChain filterChain) throws InvalidArgumentException {
        if(filterChain == null || !(filterChain instanceof AudioSimpleFilterChain)) {
            throw new InvalidArgumentException("The given filter chain must be an instance of AudioSimpleFilterChain.",
                    "La catena di filtri fornita in input deve essere un'istanza di AudioSimpleFilterChain.");
        }
        fcList.add(filterChain);
    }

    /**
     * This method removes the given filter chain from the calling instance of this class.
     * @param filterChain The given filter chain
     * @throws InvalidArgumentException If the given filter chain is not an instance of AudioSimpleFilterChain
     */
    @Override
    public void removeFilterChain(@NotNull FilterChain filterChain) throws InvalidArgumentException {
        if(filterChain == null || !(filterChain instanceof AudioSimpleFilterChain)) {
            throw new InvalidArgumentException("The given filter chain must be an instance of AudioSimpleFilterChain.",
                    "La catena di filtri fornita in input deve essere un'istanza di AudioSimpleFilterChain.");
        }
        fcList.remove(filterChain);
    }

    @Override
    public String toString() {
        List<String> helperList = new ArrayList<>();
        for(FilterChain fc: fcList) {
            helperList.add(fc.toString());
        }
        return "-af \"" + String.join(";", helperList) + "\"";
    }
}
