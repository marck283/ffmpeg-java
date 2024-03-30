package it.disi.unitn.videocreator.filtergraph;

import it.disi.unitn.videocreator.filtergraph.filterchain.SimpleFilterChain;

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

    @Override
    public String toString() {
        List<String> helperList = new ArrayList<>();
        for(SimpleFilterChain fc: sfcList) {
            helperList.add(fc.toString());
        }
        return "-vf \"" + String.join(";", helperList) + "\"";
    }

}
