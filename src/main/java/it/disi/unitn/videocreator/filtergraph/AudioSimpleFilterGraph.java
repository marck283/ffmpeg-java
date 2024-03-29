package it.disi.unitn.videocreator.filtergraph;

import it.disi.unitn.videocreator.filtergraph.filterchain.SimpleFilterChain;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to instantiate a new simple filter graph for the audio track.
 */
public class AudioSimpleFilterGraph extends SimpleFilterGraph {
    @Override
    public String toString() {
        List<String> helperList = new ArrayList<>();
        for(SimpleFilterChain fc: sfcList) {
            helperList.add(fc.toString());
        }
        return "-af \"" + String.join(";", helperList) + "\"";
    }
}
