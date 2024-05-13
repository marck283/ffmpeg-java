package it.disi.unitn.videocreator.filtergraph;

import it.disi.unitn.exceptions.InvalidArgumentException;
import it.disi.unitn.videocreator.filtergraph.filterchain.FilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.SimpleFilterChain;
import it.disi.unitn.videocreator.filtergraph.filterchain.VideoSimpleFilterChain;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to instantiate a new simple filter graph for the video track.
 */
public class VideoSimpleFilterGraph extends SimpleFilterGraph {

    /**
     * This class's constructor.
     */
    public VideoSimpleFilterGraph() {
        super();
    }

    /**
     * This method adds the given filter chain to the calling instance of this class.
     * @param filterChain The given filter chain
     * @throws InvalidArgumentException If the given filter chain is not an instance of VideoSimpleFilterChain
     */
    @Override
    public void addFilterChain(@NotNull FilterChain filterChain) throws InvalidArgumentException {
        if(filterChain == null || !(filterChain instanceof VideoSimpleFilterChain)) {
            throw new InvalidArgumentException("The given filter chain must be an instance of VideoSimpleFilterChain.",
                    "La catena di filtri fornita in input deve essere un'istanza di VideoSimpleFilterChain.");
        }
        sfcList.add((VideoSimpleFilterChain) filterChain);
    }

    /**
     * This method removes the given filter chain from the calling instance of this class.
     * @param filterChain The given filter chain
     * @throws InvalidArgumentException If the given filter chain is not an instance of VideoSimpleFilterChain
     */
    @Override
    public void removeFilterChain(@NotNull FilterChain filterChain) throws InvalidArgumentException {
        if(filterChain == null || !(filterChain instanceof VideoSimpleFilterChain)) {
            throw new InvalidArgumentException("The given filter chain must be an instance of VideoSimpleFilterChain.",
                    "La catena di filtri fornita in input deve essere un'istanza di VideoSimpleFilterChain.");
        }
        sfcList.remove(filterChain);
    }

    @Override
    public String toString() {
        List<String> helperList = new ArrayList<>();
        for(SimpleFilterChain fc: sfcList) {
            helperList.add(fc.toString());
        }
        return "-vf \"" + String.join(";", helperList) + "\"";
    }

}
